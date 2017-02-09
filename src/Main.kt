import java.io.File
import Mug.Lexer
import Mug.SpecsParser
import com.google.gson.Gson



/**
 * Created by olore on 2/4/2017.
 */

fun main(args: Array<String>){
    if (args.size == 0) {
        println("no arguments were provided!")
        return
    }

    if(File(args[0]).exists()){
        var content = File(args[0]).readText()

        var lex = Lexer(content)

        var specsParser = SpecsParser(lex)
        //lex.Tokens().forEach { print("$it\n") }
        var specsTree = specsParser.Parse()

        val gson = Gson()
        val json = gson.toJson(specsTree)
        println("json => $json")
    }

}