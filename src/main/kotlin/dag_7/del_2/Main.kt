package dag_7
import java.io.File
import kotlin.math.abs

interface Op {
    fun apply(val1: Long, val2: Long): Long
}

class Add : Op {
    override fun apply(val1: Long, val2: Long): Long {
        return val1 + val2
    }
}

class Mul : Op {
    override fun apply(val1: Long, val2: Long): Long {
        return val1 * val2
    }
}
class Com : Op {
    override fun apply(val1: Long, val2: Long): Long {
        return   (val1.toString() + val2.toString()).toLong()
    }
}

val addOp = Add()
val mulOp = Mul()
val comOp = Com()

class Task(private var answer: Long, private var inputs: List<Long>) {
    fun getAnswer(): Long {
        return answer
    }

    fun getInputs(): List<Long> {
        return inputs
    }

    private fun testOp(totSoFar:Long, remaningInputs: List<Long>, op:Op ): Boolean {
        var newTotSoFar = op.apply(totSoFar,remaningInputs[0]);
        if(newTotSoFar > answer) {
            return false
        }
        var newRemaningInputs = remaningInputs.drop(1);
        
        if(newRemaningInputs.isEmpty()) {
            return newTotSoFar == answer
        }
        return testOp(newTotSoFar, newRemaningInputs, addOp ) ||
               testOp(newTotSoFar, newRemaningInputs, mulOp ) ||
               testOp(newTotSoFar, newRemaningInputs, comOp )    

    }

    fun solve(): Boolean {
        return testOp(inputs[0], inputs.drop(1), addOp) ||
               testOp(inputs[0], inputs.drop(1), mulOp)||
               testOp(inputs[0], inputs.drop(1), comOp ) 
    }
}

fun main() {
    val tasks = File("../data.txt").readLines().map { line ->
        val (answer, inputs) = line.split(":")
        Task(answer.trim().toLong(), inputs.trim().split(" ").map { it.toLong() })
    }
    var sum : Long = 0

    for (task in tasks) {
        if(task.solve())
          sum += task.getAnswer()
    }
    println(sum)
}
 