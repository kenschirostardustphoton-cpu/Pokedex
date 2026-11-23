package com.pokedex.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pokedex.Pokedex;
import com.pokedex.PokemonModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;

@WebServlet("/api/pokemon/*")
public class PokemonServlet extends HttpServlet {

    private Pokedex pokedex;
    private Gson gson;
    private HttpClient httpClient;

    @Override
    public void init() throws ServletException {
        pokedex = new Pokedex();
        gson = new Gson();
        httpClient = HttpClient.newHttpClient();
        
        // Carrega os Pokémon da API
        carregarDadosIniciais();
        
        System.out.println("✅ Pokédex inicializada com sucesso!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String pathInfo = req.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<PokemonModel> todos = pokedex.listarTodos();
                resp.getWriter().write(gson.toJson(todos));
                return;
            }
            
            String[] partes = pathInfo.substring(1).split("/");
            
            if (partes.length == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"erro\":\"Requisição inválida\"}");
                return;
            }
            
            String acao = partes[0];
            
            switch (acao) {
                case "tipo":
                    if (partes.length == 2) {
                        String tipo = partes[1];
                        List<PokemonModel> filtrados = pokedex.buscarPorTipo(tipo);
                        resp.getWriter().write(gson.toJson(filtrados));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("{\"erro\":\"Tipo não especificado\"}");
                    }
                    break;
                    
                case "nome":
                    if (partes.length == 2) {
                        String nome = partes[1];
                        List<PokemonModel> filtrados = pokedex.buscarPorNome(nome);
                        resp.getWriter().write(gson.toJson(filtrados));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("{\"erro\":\"Nome não especificado\"}");
                    }
                    break;
                    
                case "id":
                    if (partes.length == 2) {
                        try {
                            int id = Integer.parseInt(partes[1]);
                            PokemonModel pokemon = pokedex.buscarPorId(id);
                            if (pokemon != null) {
                                resp.getWriter().write(gson.toJson(pokemon));
                            } else {
                                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                                resp.getWriter().write("{\"erro\":\"Pokémon não encontrado\"}");
                            }
                        } catch (NumberFormatException e) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("{\"erro\":\"ID inválido\"}");
                        }
                    }
                    break;
                    
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"erro\":\"Endpoint não encontrado\"}");
            }
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
    
    // 🔥 MÉTODO PARA CARREGAR OS DADOS DA API DIRETAMENTE
    private void carregarDadosIniciais() {
        System.out.println("🔄 Carregando dados da PokéAPI...");
        
        // Carrega os 151 Pokémon da 1ª geração
        for (int i = 1; i <= 151; i++) {
            adicionarPokemonDaApi(i);
        }
        
        System.out.println("✅ " + pokedex.listarTodos().size() + " Pokémon carregados!");
    }
    
    // 🔥 MÉTODO PARA TRADUZIR TIPOS (ADICIONADO)
    private String traduzirTipo(String tipoIngles) {
        if (tipoIngles == null) return null;
        
        switch (tipoIngles.toLowerCase()) {
            case "normal": return "Normal";
            case "fire": return "Fogo";
            case "water": return "Água";
            case "electric": return "Elétrico";
            case "grass": return "Planta";
            case "ice": return "Gelo";
            case "fighting": return "Lutador";
            case "poison": return "Venenoso";
            case "ground": return "Terra";
            case "flying": return "Voador";
            case "psychic": return "Psíquico";
            case "bug": return "Inseto";
            case "rock": return "Pedra";
            case "ghost": return "Fantasma";
            case "dragon": return "Dragão";
            case "dark": return "Sombrio";
            case "steel": return "Aço";
            case "fairy": return "Fada";
            default: return tipoIngles;
        }
    }
    
    private void adicionarPokemonDaApi(int id) {
        try {
            // 1. Faz a requisição HTTP para a PokéAPI
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + id))
                .header("User-Agent", "Java Pokedex")
                .timeout(java.time.Duration.ofSeconds(10))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                System.err.println("⚠️ Erro ao buscar ID " + id + ": " + response.statusCode());
                return;
            }
            
            // 2. Converte o JSON para um objeto
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            
            String nome = json.get("name").getAsString();
            
            // 3. Pega os tipos (EM INGLÊS)
            JsonArray types = json.getAsJsonArray("types");
            String tipo1Ingles = types.get(0).getAsJsonObject()
                .get("type").getAsJsonObject()
                .get("name").getAsString();
            
            String tipo2Ingles = null;
            if (types.size() > 1) {
                tipo2Ingles = types.get(1).getAsJsonObject()
                    .get("type").getAsJsonObject()
                    .get("name").getAsString();
            }

            // 🔥 TRADUZ OS TIPOS PARA PORTUGUÊS
            String tipo1 = traduzirTipo(tipo1Ingles);
            String tipo2 = traduzirTipo(tipo2Ingles);
            
            // 4. Pega os stats
            JsonArray stats = json.getAsJsonArray("stats");
            int hp = 0, ataque = 0, defesa = 0, velocidade = 0;
            
            for (int i = 0; i < stats.size(); i++) {
                JsonObject stat = stats.get(i).getAsJsonObject();
                String statName = stat.get("stat").getAsJsonObject()
                    .get("name").getAsString();
                int baseStat = stat.get("base_stat").getAsInt();
                
                switch (statName) {
                    case "hp": hp = baseStat; break;
                    case "attack": ataque = baseStat; break;
                    case "defense": defesa = baseStat; break;
                    case "speed": velocidade = baseStat; break;
                }
            }
            
            // 5. Cria o PokemonModel
            PokemonModel meuPokemon = new PokemonModel();
            meuPokemon.setNumberPokedex(id);
            meuPokemon.setNomePokemon(capitalize(nome));
            meuPokemon.setTipo(tipo1);  // JÁ TRADUZIDO
            meuPokemon.setTipo2(tipo2); // JÁ TRADUZIDO
            meuPokemon.setHp(hp);
            meuPokemon.setAtaque(ataque);
            meuPokemon.setDefesa(defesa);
            meuPokemon.setVelocidade(velocidade);
            
            // 6. Adiciona à Pokedex
            pokedex.adicionar(meuPokemon);
            
            if (id % 10 == 0) {
                System.out.println("   📦 " + id + " Pokémon carregados...");
            }
            
        } catch (Exception e) {
            System.err.println("   ⚠️ Erro ao buscar Pokémon ID " + id + ": " + e.getMessage());
        }
    }
    
    // Método para capitalizar nomes
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}