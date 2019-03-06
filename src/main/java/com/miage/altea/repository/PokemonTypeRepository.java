package com.miage.altea.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.bo.PokemonType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PokemonTypeRepository {

    private List<PokemonType> pokemons;

    public PokemonTypeRepository() {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            var pokemonsStream = classLoader.getResourceAsStream("pokemons.json");

            var objectMapper = new ObjectMapper();
            var pokemonsArray = objectMapper.readValue(pokemonsStream, PokemonType[].class);
            this.pokemons = Arrays.asList(pokemonsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonType findPokemonById(int id) {
        System.out.println("Loading Pokemon information for Pokemon id " + id);

        for(PokemonType pokemonType: pokemons){
            if(id == pokemonType.getId()){
                return pokemonType;
            }
        }
        return null;
    }

    public PokemonType findPokemonByName(String name) {
        System.out.println("Loading Pokemon information for Pokemon name " + name);

        for(PokemonType pokemon : pokemons){
            if(name.equals(pokemon.getName())){
                return pokemon;
            }
        }
        return null;
    }

    public List<PokemonType> findAllPokemon() {
        return pokemons;
    }
}
