package com.pokedex.util;

import com.pokedex.dao.EquipeDAO;
import com.pokedex.model.Equipe;

public class TesteEquipeDAO {
    public static void main(String[] args) {
        EquipeDAO dao = new EquipeDAO();

        Equipe item = new Equipe(1, 25, 1);
        boolean ok = dao.adicionar(item);
        System.out.println(ok ? "✅ Adicionado!" : "❌ Erro ao adicionar.");

        System.out.println(dao.listarPorUsuario(1).size() + " Pokemon na equipe.");
    }
}
