/**
 * Created by olore on 2/7/2017.
 */
package Mug

import Token.Token
import Token.TokenType
import java.util.*


class Lexer {
    private var content = ""
    private var currentPointer = 0
    private var row = 1
    private var column = 1
    private var symbols = HashMap<String,TokenType>()
    private val EOF: Char = '\u001a'
    private val NEW_LINE: Char = '\n'

    constructor(content: String){
        this.content = content
        SetupTokens()
    }

    private fun SetupTokens(){

        symbols.put("action",TokenType.ACTION)
        symbols.put("|",TokenType.BAR)
        symbols.put("code",TokenType.CODE)
        symbols.put(":",TokenType.COLON)
        symbols.put("::=",TokenType.COLON_COLON_EQUALS)
        symbols.put(",",TokenType.COMMA)
        symbols.put(".",TokenType.DOT)
        symbols.put("import",TokenType.IMPORT)
        symbols.put("init",TokenType.INIT)
        symbols.put("[",TokenType.LBRACK)
        symbols.put("non",TokenType.NON)
        symbols.put("nonterminal",TokenType.NONTERMINAL)
        symbols.put("package",TokenType.PACKAGE)
        symbols.put("parser",TokenType.PARSER)
        symbols.put("]",TokenType.RBRACK)
        symbols.put("scan",TokenType.SCAN)
        symbols.put(";",TokenType.SEMI)
        symbols.put("*",TokenType.STAR)
        symbols.put("start",TokenType.START)
        symbols.put("terminal",TokenType.TERMINAL)
        symbols.put("with",TokenType.WITH)
    }


    fun Tokens():List<Token>{
        var tokens = mutableListOf<Token>()
        var token: Token?
        do{
            token = nextToken()
            tokens.add(token)
        }while (token?.type!=TokenType.EOF)
        return tokens
    }

    fun nextToken():Token{
        var character = currentSymbol()
        var lexeme = ""

        while (character.isWhitespace()){
            if(character.equals(NEW_LINE)){
                row++
                column=1
            }
            else
                column++
            currentPointer++
            character= currentSymbol()
        }

        if(character.equals(NEW_LINE)){
            row++
            column=1
        }

        if(character.equals(EOF)){
            return Token("",TokenType.EOF,row,column)
        }

        if(character.equals('|')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals(':')){
            column++
            if(secondCurrentSymbol()?.equals(':') as Boolean && thirdCurrentSymbol()?.equals('=') as Boolean){
                column+=2
                currentPointer+=3
                return Token("::=", TokenType.COLON_COLON_EQUALS,row,column)
            }

            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals(',')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals('.')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals('[')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals(']')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals(';')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals('*')){
            column++
            currentPointer++
            return Token(character.toString(), symbols[character.toString()]!!,row,column)
        }

        if(character.equals('{') && secondCurrentSymbol()?.equals(':') as Boolean ){
            currentPointer+=2
            return do_code_string()
        }

        if(character.isLetter()){
            currentPointer++
            return do_id(character.toString())
        }

        return Token("",TokenType.EOF,0,0)
    }

    private fun  do_code_string(): Token {
        var character = currentSymbol()
        var _lexeme = ""
        while (!character.equals(EOF) && !(character.equals(':') && secondCurrentSymbol()?.equals('}') as Boolean)){
            _lexeme+=character
            if(character.equals(NEW_LINE)){
                row++
                column=1
            }
            column++
            currentPointer++
            character=currentSymbol()
        }
        column+=4
        if(character.equals(EOF))
            throw Exception("EOF was found!")
        currentPointer+=2
        return Token(_lexeme, TokenType.CODE_STRING,row,column)
    }

    private fun  do_id(lexeme: String): Token {
        var _lexeme = lexeme
        var character = currentSymbol()
        while (character.isLetterOrDigit() || character.equals('_') || character.equals('.') || character.equals('*')){
            _lexeme += character
            if(character.equals(NEW_LINE)){
                row++
                column=0
            }
            column++
            currentPointer++
            character=currentSymbol()
        }
        column++
        if(symbols.containsKey(_lexeme))
            return Token(_lexeme, symbols[_lexeme]!!,row,column)
        return Token(_lexeme, TokenType.ID,row,column)
    }

    private fun currentSymbol(): Char {
        if(currentPointer < content.length)
            return content[currentPointer]
        return EOF
    }

    private fun secondCurrentSymbol():Char? {
        if(currentPointer+1 < content.length)
            return content[currentPointer+1]
        return null
    }

    private fun thirdCurrentSymbol():Char? {
        if(currentPointer+2 < content.length)
            return content[currentPointer+2]
        return null
    }
}