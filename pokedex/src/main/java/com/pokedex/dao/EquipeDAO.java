package com.pokedex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pokedex.model.Equipe;
import com.pokedex.util.Conexao;

public class EquipeDAO {

    // 1. ADICIONAR POKÉMON À EQUIPE
    public boolean adicionar(Equipe item) {
        // Verifica se o usuário já tem 6 Pokémon na equipe
        if (contarPorUsuario(item.getUsuarioId()) >= 6) {
            System.out.println("⚠️ Equipe já está cheia (máximo 6 Pokémon).");
            return false;
        }

        String sql = "INSERT INTO equipe (usuario_id, pokemon_number, posicao) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getUsuarioId());
            stmt.setInt(2, item.getPokemonNumber());
            stmt.setInt(3, item.getPosicao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //2. Remover POKÉMON DA EQUIPE
    public boolean remover(int usuarioId, int pokemonNumber) {
        String sql = "DELETE FROM equipe WHERE usuario_id = ? AND pokemon_number = ?";

        try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, pokemonNumber);

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //3. Listar Todos os Pokémon da equipe (retorna lista de Equipe)
    public List<Equipe> listarPorUsuario(int usuarioId) {
        List<Equipe> equipe = new ArrayList<>();
        String sql = "SELECT * FROM equipe WHERE usuario_id = ? ORDER BY posicao";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Equipe item = new Equipe();
                item.setId(rs.getInt("id"));
                item.setUsuarioId(rs.getInt("usuario_id"));
                item.setPokemonNumber(rs.getInt("pokemon_number"));
                item.setPosicao(rs.getInt("posicao"));
                equipe.add(item);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipe;
    }

    //4. VERIFICAR SE UM POKÉMON JÁ ESTÁ NA EQUIPE
    public boolean existe(int usuarioId, int pokemonNumber) {
        String sql = "SELECT 1 FROM equipe WHERE usuario_id = ? AND pokemon_number = ?";

        try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, pokemonNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // se encontrou algum registro

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. LIMPAR TODA A EQUIPE DE UM USUÁRIO
    public boolean limparEquipe(int usuarioId) {
        String sql = "DELETE FROM equipe WHERE usuario_id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        //6. CONTAR QUANTOS POKÉMON ESTÃO NA EQUIPE
        public int contarPorUsuario(int usuarioId) {
            String sql = "SELECT COUNT(*) FROM equipe WHERE usuario_id = ?";
            try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, usuarioId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return  rs.getInt(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

}
