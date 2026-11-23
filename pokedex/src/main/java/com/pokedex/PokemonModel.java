package com.pokedex;

public class PokemonModel {
    //Variáveis de caracteristicas do pokemon
    private int numberPokedex;
    private String nomePokemon;
    private String tipo;
    private String tipo2;
    private double altura;
    private double peso;
    private String imagemUrl;
    private String[] evolucoes;

    // Variáveis de Status do pokemon
    private int hp;
    private int ataque;
    private int defesa;
    private int spAtaque;
    private int spDef;
    private int velocidade;

     /**
     * @return int return the numberPokedex
     */
    public int getNumberPokedex() {
        return numberPokedex;
    }

    public void setNumberPokedex(int numberPokedex) {
    this.numberPokedex = numberPokedex;
    }

    /**
     * @return String return the nomePokemon
     */
    public String getNomePokemon() {
        return nomePokemon;
    }

    /**
     * @param nomePokemon the nomePokemon to set
     */
    public void setNomePokemon(String nomePokemon) {
        this.nomePokemon = nomePokemon;
    }

    /**
     * @return String return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return String return the tipo2
     */
    public String getTipo2() {
        return tipo2;
    }

    /**
     * @param tipo2 the tipo2 to set
     */
    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    /**
     * @return double return the altura
     */
    public double getAltura() {
        return altura;
    }

    /**
     * @param altura the altura to set
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * @return double return the peso
     */
    public double getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * @return String return the imagemUrl
     */
    public String getImagemUrl() {
        return imagemUrl;
    }

    /**
     * @param imagemUrl the imagemUrl to set
     */
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    /**
     * @return String[] return the evolucoes
     */
    public String[] getEvolucoes() {
        return evolucoes;
    }

    /**
     * @param evolucoes the evolucoes to set
     */
    public void setEvolucoes(String[] evolucoes) {
        this.evolucoes = evolucoes;
    }

    /**
     * @return int return the hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * @param hp the hp to set
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * @return int return the ataque
     */
    public int getAtaque() {
        return ataque;
    }

    /**
     * @param ataque the ataque to set
     */
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    /**
     * @return int return the defesa
     */
    public int getDefesa() {
        return defesa;
    }

    /**
     * @param defesa the defesa to set
     */
    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    /**
     * @return int return the spAtaque
     */
    public int getSpAtaque() {
        return spAtaque;
    }

    /**
     * @param spAtaque the spAtaque to set
     */
    public void setSpAtaque(int spAtaque) {
        this.spAtaque = spAtaque;
    }

    /**
     * @return int return the spDef
     */
    public int getSpDef() {
        return spDef;
    }

    /**
     * @param spDef the spDef to set
     */
    public void setSpDef(int spDef) {
        this.spDef = spDef;
    }

    /**
     * @return int return the velocidade
     */
    public int getVelocidade() {
        return velocidade;
    }

    /**
     * @param velocidade the velocidade to set
     */
    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }


    public int getTotalStatus() {
        return this.hp + this.ataque + this.defesa + this.spAtaque + this.spDef + this.velocidade;
    }
    


   

}
