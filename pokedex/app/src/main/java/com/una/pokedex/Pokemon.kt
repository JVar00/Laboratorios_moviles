package com.una.pokedex

data class Pokemon (val id: Long, val name: String, val hp : Int, val attack: Int, val defense: Int, val speed: Int, val type: Type) {
    enum class Type {
        GRASS, WATER, FIRE, FIGHTER, ELECTRIC
    }
}