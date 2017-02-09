import java.io.File
import Mug.Lexer
import Mug.SpecsParser
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

        //println("Content $content")
    }

}