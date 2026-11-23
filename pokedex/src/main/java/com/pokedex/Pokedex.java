package com.pokedex;


import java.util.ArrayList;
import java.util.List;

public class Pokedex {
    private List<PokemonModel> pokemons;

    public Pokedex(List<PokemonModel> pokemons) {
        this.pokemons = pokemons;
    }

    public Pokedex() {
    this.pokemons = new ArrayList<>();
    }

    public List<PokemonModel> listarTodos() {
        return new ArrayList<>(pokemons);
    }

    public PokemonModel buscarPorId(int id) {
        for (PokemonModel p : pokemons) {
            if (p.getNumberPokedex() == id) {
                return p;
            }
        }
        return null;
    }

    public List<PokemonModel> buscarPorTipo(String tipo) {
        List<PokemonModel> resultado = new ArrayList<>();
        for(PokemonModel p : pokemons) {
            if (p.getTipo().equalsIgnoreCase(tipo) || (p.getTipo2() != null && p.getTipo2().equalsIgnoreCase(tipo))) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<PokemonModel> buscarPorNome(String nomePokemon) {
        List<PokemonModel> resultado = new ArrayList<>();
        for (PokemonModel p : pokemons) {
            if (p.getNomePokemon().toLowerCase().contains(nomePokemon.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public boolean adicionar(PokemonModel pokemon) {
        if (pokemon == null) {
        return false;
        }

        if(buscarPorId(pokemon.getNumberPokedex()) !=null) {
            return false;
        }
        pokemons.add(pokemon);
        return true;
    }

        public boolean remover(int id) {
        PokemonModel pokemon = buscarPorId(id);
        if (pokemon != null) {
            pokemons.remove(pokemon);
            return true;
        }
        return false;
    }

        public int tamanho() {
        return pokemons.size();
    }
}
