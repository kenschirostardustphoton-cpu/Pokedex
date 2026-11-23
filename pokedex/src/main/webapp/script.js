const API_BASE_URL = 'http://localhost:8080/api/pokemon';

document.addEventListener('DOMContentLoaded', function() {
    carregarTodos();
});

document.getElementById('searchInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        buscarPokemon();
    }
});

function carregarTodos() {
    mostrarLoading(true);
    document.getElementById('tipoFilter').value = '';

    fetch(API_BASE_URL)
    .then(response => response.json())
    .then(pokemons => {
        exibirPokemons(pokemons);
        mostrarLoading(false);
    })
    .catch(error => {
        console.error('Erro:', error);
        mostrarLoading(false);
        document.getElementById('pokemonGrid').innerHTML = '<p style="color:white;text-align:center;font-size:18px;">❌ Erro ao carregar Pokémon. Tente novamente.</p>';
    });
    
}

function buscarPokemon() {
    const input = document.getElementById('searchInput');
    const nome = input.value.trim();

    if (!nome) {
        carregarTodos();
        return;
    }

    mostrarLoading(true);

    fetch(`${API_BASE_URL}/nome/${encodeURIComponent(nome)}`)
    .then(response => response.json())
    .then(pokemons => {
        exibirPokemons(pokemons);
        mostrarLoading(false);
        if (pokemons.length === 0) {
            document.getElementById('pokemonGrid').innerHTML =                     '<p style="color:white;text-align:center;font-size:18px;">🔍 Nenhum Pokémon encontrado com esse nome.</p>';
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        mostrarLoading(false);
    });
}

function filtrarPorTipo() {
    const tipo = document.getElementById('tipoFilter').value;

    if(!tipo) {
        carregarTodos();
        return;
    }

    mostrarLoading(true);

    fetch(`${API_BASE_URL}/tipo/${tipo}`)
    .then(response => response.json())
    .then(pokemons => {
        exibirPokemons(pokemons);
        mostrarLoading(false);
        if (pokemons.length === 0) {
            document.getElementById('pokemonGrid').innerHTML = `<p style="color:white;text-align:center;font-size:18px;">🔍 Nenhum Pokémon do tipo ${tipo} encontrado.</p>`;
        }
    })
    .catch(error => {
        console.error('Erro:', error);
    });
}

function exibirPokemons(pokemons) {
    const grid = document.getElementById('pokemonGrid');
    grid.innerHTML = '';  // Limpa a tela
    
    if (!pokemons || pokemons.length === 0) {  // Se não tem Pokémon
        grid.innerHTML = '<p style="color:white;text-align:center;font-size:18px;">Nenhum Pokémon encontrado.</p>';
        return;
    }
    
    pokemons.forEach(pokemon => {  // Para cada Pokémon na lista
        const card = criarCard(pokemon);  // Cria um card
        grid.appendChild(card);  // Adiciona o card na tela
    });
}

function criarCard(pokemon) {
    const card = document.createElement('div');
    card.className = 'pokemon-card';
    card.onclick = () => abrirModal(pokemon.numberPokedex);

    const imageUrl = `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.numberPokedex}.png`;

    const tipos = [pokemon.tipo];
    if (pokemon.tipo2) tipos.push(pokemon.tipo2);

    let tiposHtml = tipos.map(tipo => `<span class="type-badge type-${tipo}">${tipo}</span>`).join('');

    // Monta o HTML do card
    card.innerHTML = `
        <div class="pokemon-id">#${String(pokemon.numberPokedex).padStart(3, '0')}</div>
        <img src="${imageUrl}" alt="${pokemon.nomePokemon}" 
             onerror="this.src='https://via.placeholder.com/96?text=??'">
        <div class="pokemon-name">${pokemon.nomePokemon}</div>
        <div class="pokemon-types">${tiposHtml}</div>
    `;

    return card;
}

function abrirModal(id) {
    fetch(`${API_BASE_URL}/id/${id}`)  // Busca o Pokémon específico
        .then(response => response.json())
        .then(pokemon => {
            if (pokemon) {
                // Busca dados extras (descrição, evolução) na PokéAPI
                buscarDadosAdicionais(id, pokemon);
            }
        })
        .catch(error => {
            console.error('Erro ao buscar Pokémon:', error);
        });

}

function buscarDadosAdicionais(id, pokemon) {
    fetch(`https://pokeapi.co/api/v2/pokemon-species/${id}`)
        .then(response => response.json())
        .then(data => {
            // --- 1. DESCRIÇÃO ---
            let descricao = 'Descrição não disponível';
            if (data.flavor_text_entries) {
                // Tenta português primeiro
                for (let entry of data.flavor_text_entries) {
                    if (entry.language.name === 'pt') {
                        descricao = entry.flavor_text;
                        break;
                    }
                }
                // Se não achou em português, tenta inglês
                if (descricao === 'Descrição não disponível') {
                    for (let entry of data.flavor_text_entries) {
                        if (entry.language.name === 'en') {
                            descricao = entry.flavor_text;
                            break;
                        }
                    }
                }
            }

            // --- 2. EVOLUÇÃO ---
            if (data.evolution_chain && data.evolution_chain.url) {
                // Busca a cadeia de evolução
                fetch(data.evolution_chain.url)
                    .then(res => res.json())
                    .then(chainData => {
                        // Obtém a cadeia completa com detalhes
                        const evolucoes = extrairEvolucoes(chainData.chain);
                        // Chama a exibição passando o array de evoluções
                        exibirDetalhesCompletos(pokemon, descricao, evolucoes);
                    })
                    .catch(err => {
                        console.error('Erro ao buscar evolução:', err);
                        exibirDetalhesCompletos(pokemon, descricao, 'Sem evolução');
                    });
            } else {
                // Se não houver cadeia de evolução
                exibirDetalhesCompletos(pokemon, descricao, 'Sem evolução');
            }
        })
        .catch(error => {
            console.error('Erro ao buscar dados adicionais:', error);
            exibirDetalhesCompletos(pokemon, 'Descrição não disponível', 'Sem evolução');
        });
}

function extrairEvolucoes(chain) {
    let evolucoes = [];

    function percorrer(node,detalhesAnteriores) {
        if (!node) return;

        let detalhes = null;
        if (node.evolution_details && node.evolution_details.length > 0) {
            detalhes = node.evolution_details[0];
        }

        evolucoes.push({
            nome : node.species.name,
            detalhes: detalhes
        });

        if(node.evolves_to && node.evolves_to.length > 0) {
            for (let evolucao of node.evolves_to) {
                percorrer(evolucao);
            }
        }

    }

    percorrer (chain);

    //Converte para um formato com método de evolução
    return evolucoes.map((item, index) => {
        const nome = capitalize(item.nome);
        let metodo = '';

        if(index === 0) {
            return { nome, metodo: null, isPrimeira:true };
        }

        if(item.detalhes) {
            metodo = extrairMetodoEvolucao(item.detalhes);
        }

        return { nome, metodo, isPrimeira: false};
    });

}

function extrairMetodoEvolucao(detalhes) {
    if (!detalhes) return 'Evolução desconhecida';

    const trigger = detalhes.trigger ? detalhes.trigger.name : null;

    switch (trigger) {
        case 'level-up' :
            let nivel = detalhes.min_level || '?';
            return `Nível ${nivel}`;
        case 'use-item':
            let item = detalhes.item ? detalhes.item.name : 'item desconhecido';
            return `Usar ${traduzirItem(item)}`;
        case 'trade' :
            return 'Troca';
        case 'shed':
            return 'Evolução especial (casa)';
        default :
            if (detalhes.min_level) return `Nível ${detalhes.min_level}`;
            if (detalhes.item) return `Usar ${traduzirItem(detalhes.item.name)}`;
                return 'Evolução especial';
    }
}

function traduzirItem(itemIngles) {
    const itens = {
        'thunder-stone': 'Pedra do Trovão ⚡',
        'fire-stone': 'Pedra de Fogo 🔥',
        'water-stone': 'Pedra de Água 💧',
        'leaf-stone': 'Pedra de Folha 🌿',
        'moon-stone': 'Pedra da Lua 🌙',
        'sun-stone': 'Pedra do Sol ☀️',
        'shiny-stone': 'Pedra Brilhante ✨',
        'dusk-stone': 'Pedra do Crepúsculo 🌆',
        'dawn-stone': 'Pedra do Amanhecer 🌅',
        'ice-stone': 'Pedra de Gelo ❄️',
        'king\'s-rock': 'Rocha do Rei 👑',
        'metal-coat': 'Revestimento Metálico',
        'dragon-scale': 'Escama de Dragão 🐉',
        'up-grade': 'Up-Grade',
        'protector': 'Protetor',
        'electirizer': 'Eletrizador',
        'magmarizer': 'Magmarizador',
        'razor-claw': 'Garra Fina',
        'razor-fang': 'Presa Fina',
        'reaper-cloth': 'Pano Sinistro',
        'prism-scale': 'Escama Prisma',
    };
    return itens[itemIngles] || itemIngles;
}

function montarEvolucaoHtml(evolucoes) {
    if (!evolucoes || evolucoes.length === 0) {
        return 'Sem evolução';
    }

    if (Array.isArray(evolucoes)) {
        return evolucoes.map((item, index) => {
            let texto = item.nome;  // ← AGORA TUDO COM 'texto'
            if (item.metodo && !item.isPrimeira) {
                texto += ` <span style="color:#667eea;font-size:0.9em;">(${item.metodo})</span>`;
            }
            return texto;
        }).join(' → ');
    }

    return evolucoes;
}

function exibirDetalhesCompletos(pokemon, descricao, evolucao) {
    const modalContent = document.getElementById('detalhesPokemon');

    const imageUrl = `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.numberPokedex}.png`;

    const tipos = [pokemon.tipo];
    if (pokemon.tipo2) tipos.push(pokemon.tipo2);

    let tiposHtml = tipos.map(tipo => 
        `<span class="type-badge type-${tipo}">${tipo}</span>`
    ).join('');

    const evolucaoHtml = montarEvolucaoHtml(evolucao);
    
    modalContent.innerHTML= `
    <div class="modal-header">
            <img src="${imageUrl}" alt="${pokemon.nomePokemon}"
                 onerror="this.src='https://via.placeholder.com/150?text=??'">
            <h2>#${String(pokemon.numberPokedex).padStart(3, '0')} - ${pokemon.nomePokemon}</h2>
        </div>
        
        <div class="modal-types">${tiposHtml}</div>
        
        <div class="modal-section">
            <h3>📖 Descrição</h3>
            <p>${descricao || 'Descrição não disponível'}</p>
        </div>
        
        <div class="modal-section">
            <h3>📊 Status Base</h3>
            <div class="pokemon-stats">
                <div class="stat-item"><span class="stat-label">❤️ HP:</span> ${pokemon.hp}</div>
                <div class="stat-item"><span class="stat-label">⚔️ Ataque:</span> ${pokemon.ataque}</div>
                <div class="stat-item"><span class="stat-label">🛡️ Defesa:</span> ${pokemon.defesa}</div>
                <div class="stat-item"><span class="stat-label">💨 Velocidade:</span> ${pokemon.velocidade}</div>
            </div>
        </div>
        
        <div class="modal-section">
            <h3>🔄 Evolução</h3>
            <p>${montarEvolucaoHtml(evolucao)}</p>
        </div>
        
        ${pokemon.altura ? `<p>📏 Altura: ${pokemon.altura}m</p>` : ''}
        ${pokemon.peso ? `<p>⚖️ Peso: ${pokemon.peso}kg</p>` : ''}
    `;

    // Abre o modal
    document.getElementById('modal').style.display = 'block';
}

function fecharModal() {
    document.getElementById('modal').style.display = 'none';  // Fecha o modal
}

// Fechar modal clicando fora
window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        fecharModal();
    }
};

function mostrarLoading(mostrar) {
    document.getElementById('loading').style.display = mostrar ? 'block' : 'none';
}


function capitalize(str) {
    if (!str) return str;
    return str.charAt(0).toUpperCase() + str.slice(1);
}