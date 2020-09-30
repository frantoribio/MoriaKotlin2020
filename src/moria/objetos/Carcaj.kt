package moria.objetos

class Carcaj(override var tipo: String = "Carcaj", var cantidad: Int = 1) : Objeto(tipo) {

    override fun test() {
        println("Soy el objeto $tipo y mis caracteristica es cantidad: $cantidad")
    }
}