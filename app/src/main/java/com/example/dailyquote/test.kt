package com.example.dailyquote

fun main() {



}

open class Vehicle(private val name: String, private val wheels: Int) {
    fun printWheels() = println("$name has $wheels wheels")
}
 interface ElectricVehicle {

    fun charge() {

    }
}

class Car(name: String) : Vehicle(name, 4), ElectricVehicle

class Scooter(name: String) : Vehicle(name, 2)