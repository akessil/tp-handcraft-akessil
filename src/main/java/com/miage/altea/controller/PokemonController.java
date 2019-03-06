package com.miage.altea.controller;

import com.miage.altea.annotation.RequestMapping;
import com.miage.altea.repository.PokemonTypeRepository;
import com.sun.net.httpserver.HttpsParameters;

import java.util.Map;

@Controller
public class PokemonController {

    private PokemonTypeRepository repo = new PokemonTypeRepository();

    @RequestMapping(uri="/pokemon")
    public String getPokemon(Map<String,String[]> parameters){
        if(parameters.get("id")!=null){
            int id =Integer.parseInt(parameters.get("id")[0]);
            return repo.findPokemonById(id).getName();
        }
        return "NOT FOUND Pokemon";
    }
}
