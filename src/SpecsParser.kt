

/**
 * Created by olore on 2/8/2017.
 */
package Mug

import Token.TokenType
import Token.Token

class SpecsParser {
    private var  lexer: Lexer
    private var  _currentToken: Token

    constructor(lex: Lexer){
        lexer = lex
        _currentToken = lexer.nextToken()

    }

    fun  Parse(): Any? {

        var javaMugSpec = java_cup_spec()
        if(!_currentToken.IsEOF())
            throw Exception("EOF was expected!")

        return null
    }

    private fun  java_cup_spec() {
        package_spec()
        import_list()
        code_parts()
        symbol_list()
        start_spec()
        production_list()
    }

    private fun package_spec() {
        if(_currentToken.IsPackage()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                _currentToken=lexer.nextToken()
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                }else{
                    throw Exception("an ; was expected!")
                }
            }else{
                throw Exception("an id was expected!")
            }
        }
        //empty
    }

    private fun import_list() {
        if(_currentToken.IsImport()){
            import_spec()
            import_list()
        }
        //empty
    }

    private fun import_spec() {
        if(_currentToken.IsImport()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                import_id()
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                }else{
                    throw Exception("an ; was expected!")
                }
            }else{
                throw Exception("an id was expected!")
            }
        }else{
            throw Exception("an import was expected!")
        }
    }

    private fun import_id() {
        multipart_id()
        opt_dot_start()
    }

    private fun opt_dot_start() {
        if(_currentToken.IsDot()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsStar()){
                _currentToken=lexer.nextToken()
            }else{
                throw Exception("a * was expected!")
            }
        }
    }

    private fun code_parts() {
        if(_currentToken.IsCodePart()){
            code_part()
            code_parts()
        }
        //empty
    }

    private fun code_part() {
        if(_currentToken.IsAction()){
            action_code_part()
        }else if(_currentToken.IsParser()){
            parser_code_part()
        }else if(_currentToken.IsInit()){
            init_code()
        }else if(_currentToken.IsScan()){
            scan_code()
        }else{
            throw Exception("a code part(action,parser,init,scan) was expected!")
        }
    }

    private fun action_code_part() {
        _currentToken=lexer.nextToken()
        if(_currentToken.IsCode()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCodeString()){
                _currentToken=lexer.nextToken()
                opt_semi()
            }
        }
    }

    private fun opt_semi() {
        if(_currentToken.IsSemi()){
            _currentToken=lexer.nextToken()
        }
        //empty
    }

    private fun parser_code_part() {
        _currentToken=lexer.nextToken()
        if(_currentToken.IsCode()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCodeString()){
                _currentToken=lexer.nextToken()
                opt_semi()
            }
        }
    }

    private fun init_code() {
        _currentToken=lexer.nextToken()
        if(_currentToken.IsWith()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCodeString()){
                _currentToken=lexer.nextToken()
                opt_semi()
            }
        }
    }

    private fun scan_code() {
        _currentToken=lexer.nextToken()
        if(_currentToken.IsWith()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCodeString()){
                _currentToken=lexer.nextToken()
                opt_semi()
            }
        }
    }

    private fun symbol_list() {
        symbol()
        symbol_list_prime()
    }

    private fun symbol() {
        if(_currentToken.IsTerminal()){
            _currentToken=lexer.nextToken()
            opt_type_id()
            if(_currentToken.IsComma())
            {
                _currentToken=lexer.nextToken()
                //si se encuentra una comma, el id recibido en opt_type_id va a formar parse de el resultado de declares term
                declares_term()
            }else if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
            }else{
                declares_term()
            }
        }else if(_currentToken.IsNon()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsTerminal()){
                _currentToken=lexer.nextToken()
                opt_type_id()
                if(_currentToken.IsComma())
                {
                    _currentToken=lexer.nextToken()
                    //si se encuentra una comma, el id recibido en opt_type_id va a formar parse de el resultado de declares term
                    declares_non_term()
                }else if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                }else{
                    declares_non_term()
                }
            }
        }else if(_currentToken.IsNonTerminal()){
            _currentToken=lexer.nextToken()
            opt_type_id()
            if(_currentToken.IsComma())
            {
                _currentToken=lexer.nextToken()
                //si se encuentra una comma, el id recibido en opt_type_id va a formar parse de el resultado de declares term
                declares_non_term()
            }else if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
            }else{
                declares_non_term()
            }
        }else{
            throw Exception("a symbol was expected!")
        }
    }

    private fun declares_non_term() {
        non_term_name_list()
        if(_currentToken.IsSemi()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("a ; was expected")
        }
    }

    private fun non_term_name_list() {
        new_non_term_id()
        non_term_name_list_prime()
    }

    private fun new_non_term_id() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun non_term_name_list_prime() {
        if(_currentToken.IsComma()){
            _currentToken=lexer.nextToken()
            new_non_term_id()
            non_term_name_list_prime()
        }
        //empty
    }

    private fun opt_type_id() {
        if(_currentToken.IsId()){
            type_id()
        }
        //empty
    }

    private fun type_id() {
        multipart_id()
    }

    private fun multipart_id() {
        if(_currentToken.IsId()){
            multipart_id_prime()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun multipart_id_prime() {
        _currentToken=lexer.nextToken()
        if(_currentToken.IsDot()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                multipart_id_prime()
            }else{
                throw Exception("an id was expected!")
            }
        }
        //empty
    }

    private fun declares_term() {
        term_name_list()
        if(_currentToken.IsSemi()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("a ; was expected")
        }
    }

    private fun term_name_list() {
        new_term_id()
        term_name_list_prime()
    }

    private fun new_term_id() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun term_name_list_prime() {
        if(_currentToken.IsComma()){
            _currentToken=lexer.nextToken()
            new_term_id()
            term_name_list_prime()
        }
        //empty
    }

    private fun symbol_list_prime() {
        if(_currentToken.IsSymbol()){
            symbol()
            symbol_list_prime()
        }
        //empty
    }

    private fun start_spec() {
        if(_currentToken.IsStart()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsWith()){
                _currentToken=lexer.nextToken()
                nt_id()
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                }else{
                    throw Exception("a ; was expected!")
                }
            }else{
                throw Exception("the 'with' keyword was expected!")
            }
        }
        //empty
    }

    private fun nt_id() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun production_list() {
        production()
        production_list_prime()
    }

    private fun production() {
        nt_id()
        if(_currentToken.IsColonColonEquals()){
            _currentToken=lexer.nextToken()
            rhs_list()
            if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
            }else{
                throw Exception("a ; was expected!")
            }
        }else{
            throw Exception("a ::= was expected!")
        }
    }

    private fun rhs_list() {
        rhs()
        rhs_list_prime()
    }

    private fun rhs() {
        prod_part_list()
        opt_term_id()
    }

    private fun prod_part_list() {
        if(_currentToken.IsProdPart()){
            prod_part()
            prod_part_list()
        }
        //empty
    }

    private fun prod_part() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
            opt_label()
        }else if(_currentToken.IsCodeString()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("a production part was expected!")
        }
    }

    private fun opt_label() {
        if(_currentToken.IsColon()){
            _currentToken=lexer.nextToken()
            label_id()
        }
        //empty
    }

    private fun label_id() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun opt_term_id() {
        if(_currentToken.IsId()){
            term_id()
        }
        //empty
    }

    private fun term_id() {
        symbol_id()
    }

    private fun symbol_id() {
        if(_currentToken.IsId()){
            _currentToken=lexer.nextToken()
        }else{
            throw Exception("an id was expected!")
        }
    }

    private fun rhs_list_prime() {
        if(_currentToken.IsBar()){
            _currentToken=lexer.nextToken()
            rhs()
            rhs_list_prime()
        }
        //empty
    }

    private fun production_list_prime() {
        if(_currentToken.IsId()){
            production()
            production_list_prime()
        }
        //empty
    }


}