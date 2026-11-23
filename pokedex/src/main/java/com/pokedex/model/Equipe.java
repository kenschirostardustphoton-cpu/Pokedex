package com.pokedex.model;

public class Equipe {
    private int id;
    private int usuarioId;
    private int pokemonNumber;
    private int posicao;

        public Equipe() {
    }

    // Construtor com todos os parâmetros (exceto id, que é auto-increment)
    public Equipe(int usuarioId, int pokemonNumber, int posicao) {
        this.usuarioId = usuarioId;
        this.pokemonNumber = pokemonNumber;
        this.posicao = posicao;
    }

    // Construtor completo (incluindo id)
    public Equipe(int id, int usuarioId, int pokemonNumber, int posicao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pokemonNumber = pokemonNumber;
        this.posicao = posicao;
    }

    /**
     * @return ind return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return int return the usuarioId
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return int return the pokemonNumber
     */
    public int getPokemonNumber() {
        return pokemonNumber;
    }

    /**
     * @param pokemonNumber the pokemonNumber to set
     */
    public void setPokemonNumber(int pokemonNumber) {
        this.pokemonNumber = pokemonNumber;
    }

    /**
     * @return int return the posicao
     */
    public int getPosicao() {
        return posicao;
    }

    /**
     * @param posicao the posicao to set
     */
    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

}
