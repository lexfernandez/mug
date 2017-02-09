/**
 * Created by olore on 2/7/2017.
 */

package Token

enum class TokenType {
    ACTION,
    BAR,
    CODE ,
    CODE_STRING,
    COLON,
    COLON_COLON_EQUALS,
    COMMA,
    DOT ,
    EOF,
    ID ,
    IMPORT,
    INIT,
    LBRACK,
    NON,
    NONTERMINAL,
    PACKAGE,
    PARSER ,
    RBRACK,
    SCAN ,
    SEMI,
    STAR,
    START,
    TERMINAL,
    WITH
}

class Token(val lexeme: String, val type: TokenType, val row: Int, val column: Int){

    override fun toString(): String {
        return "($row,$column) => $type"
    }

    fun  IsCodePart(): Boolean {
        if(IsAction() || IsParser() || IsInit() || IsScan()){
            return true
        }
        return false
    }

    fun  IsSymbol(): Boolean {
        if(type.equals(TokenType.TERMINAL) || type.equals(TokenType.NON) || type.equals(TokenType.NONTERMINAL)){
            return true
        }
        return false
    }

    fun  IsId(): Boolean {
        if(type.equals(TokenType.ID)){
            return true
        }
        return false
    }

    fun  IsSemi(): Boolean {
        if(type.equals(TokenType.SEMI)){
            return true
        }
        return false
    }

    fun  IsComma(): Boolean {
        if(type.equals(TokenType.COMMA)){
            return true
        }
        return false
    }

    fun IsColonColonEquals(): Boolean {
        if(type.equals(TokenType.COLON_COLON_EQUALS)){
            return true
        }
        return false
    }

    fun  IsProdPart(): Boolean {
        if(IsId() || IsCodeString()){
            return true
        }
        return false
    }

    fun IsCodeString(): Boolean {
        if(type.equals(TokenType.CODE_STRING)){
            return true
        }
        return false
    }

    fun IsColon(): Boolean {
        if(type.equals(TokenType.COLON)){
            return true
        }
        return false
    }

    fun IsDot(): Boolean {
        if(type.equals(TokenType.DOT)){
            return true
        }
        return false
    }

    fun  IsStar(): Boolean {
        if(type.equals(TokenType.START)){
            return true
        }
        return false
    }

    fun IsScan(): Boolean {
        if(type.equals(TokenType.SCAN)){
            return true
        }
        return false
    }

    fun IsInit(): Boolean {
        if(type.equals(TokenType.INIT)){
            return true
        }
        return false
    }

    fun IsParser(): Boolean {
        if(type.equals(TokenType.PARSER)){
            return true
        }
        return false
    }

    fun IsAction(): Boolean {
        if(type.equals(TokenType.ACTION)){
            return true
        }
        return false
    }

    fun IsBar(): Boolean {
        if(type.equals(TokenType.BAR)){
            return true
        }
        return false
    }

    fun IsWith(): Boolean {
        if(type.equals(TokenType.WITH)){
            return true
        }
        return false
    }

    fun  IsStart(): Boolean {
        if(type.equals(TokenType.START)){
            return true
        }
        return false
    }

    fun IsNonTerminal(): Boolean {
        if(type.equals(TokenType.NONTERMINAL)){
            return true
        }
        return false
    }

    fun IsTerminal(): Boolean {
        if(type.equals(TokenType.TERMINAL)){
            return true
        }
        return false
    }

    fun IsNon(): Boolean {
        if(type.equals(TokenType.NON)){
            return true
        }
        return false
    }

    fun IsCode(): Boolean {
        if(type.equals(TokenType.CODE)){
            return true
        }
        return false
    }

    fun  IsImport(): Boolean {
        if(type.equals(TokenType.IMPORT)){
            return true
        }
        return false
    }

    fun IsPackage(): Boolean {
        if(type.equals(TokenType.PACKAGE)){
            return true
        }
        return false
    }

    fun IsEOF(): Boolean {
        if(type.equals(TokenType.EOF)){
            return true
        }
        return false
    }


}