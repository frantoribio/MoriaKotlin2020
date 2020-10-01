package moria

import moria.objetos.Anillo
import moria.objetos.Carcaj
import moria.objetos.Vara
import moria.peligros.Accion
import moria.peligros.Habilidad
import moria.peligros.Magico
import moria.personajes.Elfo
import moria.personajes.Hobbit
import moria.personajes.Mago
import moria.personajes.Personaje
import moria.salas.Sala
import kotlin.random.Random

// Definimos Moria como Object y de esta manera implementamos el singleton
// https://blog.mindorks.com/how-to-create-a-singleton-class-in-kotlin
// https://refactoring.guru/es/design-patterns/singleton
object Moria {
    // Constantes del sistema.
    // Parametrización
    private const val MAX_ENERGIA = 50
    private const val MAX_FLECHAS = 20
    private const val MAX_SALAS = 36
    private const val MAX_SALA_MALIGNO = 10
    private const val MAX_SALA_FLECHAS = 10
    private const val MAX_SALA_ENEMIGOS = 10

    // Condiciones del run
    private const val CONTINUAR = true
    private const val TERMINAR = false

    // Variables de Moria, con lateint indicamos que las inicializaremos fuera del int o de la declaracion
    // Personajes
    private lateinit var gandalf: Personaje
    private lateinit var legolas: Personaje
    private lateinit var frodo: Personaje

    // Lista de salas
    private var salas = mutableListOf<Sala>()

    // Variables de ejecución
    private lateinit var salaActual: Sala
    private var estado = CONTINUAR

    // Me gusta definir las cosas en el init para evitar ensuciar el código
    // a diferencia con constructor es que este esta pensado para tareas mas "cargadas" y una vez creado el objeto
    // Le asigna los valores que queramos
    init {
        initPersonajes()
        initSalas()
    }

    // Inicia los personajes
    private fun initPersonajes() {
        // Como vemos estamos realizando una inyección de dependencias usando agragaciones con objetos asbtractos
        gandalf = Mago("Gandalf", true, Vara(energia = Random.nextInt(1, MAX_ENERGIA)))
        legolas = Elfo("Legolas", true, Carcaj(cantidad = Random.nextInt(1, MAX_FLECHAS)))
        frodo = Hobbit(nombre = "Frodo", vivo = true, objeto = Anillo())
    }

    // Inicia la salas
    private fun initSalas() {
        // Como es Fifo añadimos siempre al final
        for (i in 1..MAX_SALAS) {
            when (Random.nextInt(1, 4)) {
                1 -> salas.add(Sala(i, Magico(poder = Random.nextInt(1, MAX_SALA_MALIGNO))))
                2 -> salas.add(
                    Sala(
                        i,
                        Accion(
                            flechas = Random.nextInt(1, MAX_SALA_FLECHAS),
                            enemigos = Random.nextInt(1, MAX_SALA_ENEMIGOS)
                        )
                    )
                )
                3 -> salas.add(Sala(i, Habilidad()))
            }
        }
    }

    // función de ejecución
    fun run() {
        println("Abriendo las puertas de Moria")
        // mientras haya salas o no hayamos terminado
        while (salas.size >= 1 && estado == CONTINUAR) {
            // entramos en la sala
            entrarSala()
            // Analizamos el tipo de peligro y con ella actuamos,
            estado = analizarActuar()

            println("sala ${this.salaActual.numero}")
        }
    }

    private fun analizarActuar(): Boolean {
        // Podemos usar el casting con la variable tipo pero mejor hacemos casting
        when (this.salaActual.peligro) {
            is Magico -> return accionMago()
            is Accion -> return accionElfo()
            is Habilidad -> return frodo.accion()
            else -> return TERMINAR
        }
    }

    private fun accionElfo(): Boolean {
        println("Elfo entra en acción")
        return true
    }

    private fun accionMago(): Boolean {
        println("Mago entra en acción")
        return true
    }

    private fun entrarSala() {
        // Eliminamos como la primera porque es una estructura FIFO
        this.salaActual = salas.removeAt(0)
        println("--> Entrando en la sala nº: ${this.salaActual.numero}. Es del tipo: ${this.salaActual.peligro.tipo}")
    }

    // funcion de test
    fun test() {
        println("Soy Moria")
        gandalf.test()
        gandalf.objeto.test()
        legolas.test()
        legolas.objeto.test()
        frodo.test()
        frodo.objeto.test()

        val mago = Mago("El Mago", vivo = true, Vara())
        mago.test()
        mago.recargarVara(20)
        mago.objeto.test()
        val elfo = Elfo("El Elfo", vivo = true, Carcaj())
        elfo.test()
        elfo.recargarCarcaj(10)
        elfo.objeto.test()
        val hobbit = Hobbit("El Hobbit", vivo = true, Anillo())
        hobbit.test()
        hobbit.ponerseAnillo()
        hobbit.objeto.test()


        salas.forEach {
            it.test()
            it.peligro.test()
            println(hobbit.huir())
        }
    }
}