/**
 * Created by olore on 2/8/2017.
 */
package Mug

import Token.Token

class SpecsParser {
    private var  lexer: Lexer
    private var  _currentToken: Token

    constructor(lex: Lexer){
        lexer = lex
        _currentToken = lexer.nextToken()

    }

    fun  Parse(): MugSpec {

        var mugSpec = mug_spec()
        if(!_currentToken.IsEOF())
            throw Exception("EOF was expected!\n${_currentToken.error()}")

        return mugSpec
    }

    private fun mug_spec(): MugSpec {
        var packageSpec = package_spec()
        var importList = import_list()
        var codeParts = code_parts()
        var symbolList = symbol_list()
        //start_spec()
        var productionList = production_list()
        return MugSpec(packageSpec, importList, codeParts, symbolList, productionList)
    }

    private fun package_spec() : Package? {
        if(_currentToken.IsPackage()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                var id = multipart_id()
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return Package(id)
                }else{
                    throw Exception("an ; was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("an id was expected!\n${_currentToken.error()}")
            }
        }
        //empty
        return null
    }

    private fun import_list(): MutableList<ImportSpec> {
        if(_currentToken.IsImport()){
            var importSpec = import_spec()
            var importList = import_list()
            importList.add(0,importSpec)
            return  importList
        }
        //empty
        return mutableListOf()
    }

    private fun import_spec(): ImportSpec {
        if(_currentToken.IsImport()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                var id=import_id()
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return ImportSpec(id)
                }else{
                    throw Exception("an ; was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("an id was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("an import was expected!\n${_currentToken.error()}")
        }
    }

    private fun import_id(): Id {
        if(_currentToken.IsId()){
            var lexeme = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return import_multipart_id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun import_multipart_id(lexeme: String): Id {
        if(_currentToken.IsDot()){
            var dot = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return import_multipart_id_prime("$lexeme$dot")
        }
        //empty
        return Id(lexeme)
    }

    private fun import_multipart_id_prime(lexeme: String): Id {
        if(_currentToken.IsId()){
            var id = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return import_multipart_id("$lexeme$id")
        }else if(_currentToken.IsStar()){
            var star = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id("$lexeme$star")
        }else{
            throw Exception("an id or * token was expected!\n${_currentToken.error()}")
        }
    }

    private fun code_parts() : MutableList<CodePart>  {
        if(_currentToken.IsCodePart()){
            var codePart = code_part()
            var codeParts = code_parts()
            codeParts.add(0,codePart)
            return codeParts
        }
        //empty
        return mutableListOf()
    }

    private fun code_part() : CodePart {
        if(_currentToken.IsAction()){
            return action_code_part()
        }else if(_currentToken.IsParser()){
            return parser_code_part()
        }else if(_currentToken.IsInit()){
            return init_code()
        }else if(_currentToken.IsScan()){
            return scan_code()
        }else{
            throw Exception("a code part(action,parser,init,scan) was expected!\n${_currentToken.error()}")
        }
    }

    private fun action_code_part(): ActionCode {
        if(_currentToken.IsAction()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCode()){
                _currentToken=lexer.nextToken()
                if(_currentToken.IsCodeString()){
                    var codeString = _currentToken.lexeme
                    _currentToken=lexer.nextToken()
                    opt_semi()
                    return ActionCode(codeString)
                }else{
                    throw Exception("a code string was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("the 'code' keyword was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("the 'action' keyword was expected!\n${_currentToken.error()}")
        }
    }

    private fun opt_semi() {
        if(_currentToken.IsSemi()){
            _currentToken=lexer.nextToken()
        }
        //empty
    }

    private fun parser_code_part(): ParserCode {
        if(_currentToken.IsParser()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsCode()){
                _currentToken=lexer.nextToken()
                if(_currentToken.IsCodeString()){
                    var codeString = _currentToken.lexeme
                    _currentToken=lexer.nextToken()
                    opt_semi()
                    return ParserCode(codeString)
                }else{
                    throw Exception("a code string was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("the 'code' keyword was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("the 'parser' keyword was expected!\n${_currentToken.error()}")
        }
    }

    private fun init_code(): InitWith {
        if(_currentToken.IsInit()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsWith()){
                _currentToken=lexer.nextToken()
                if(_currentToken.IsCodeString()){
                    var codeString = _currentToken.lexeme
                    _currentToken=lexer.nextToken()
                    opt_semi()
                    return InitWith(codeString)
                }else{
                    throw Exception("a code string was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("the 'width' keyword was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("the 'init' keyword was expected!\n${_currentToken.error()}")
        }
    }

    private fun scan_code(): ScanWith {
        if(_currentToken.IsScan()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsWith()){
                _currentToken=lexer.nextToken()
                if(_currentToken.IsCodeString()){
                    var codeString = _currentToken.lexeme
                    _currentToken=lexer.nextToken()
                    opt_semi()
                    return ScanWith(codeString)
                }else{
                    throw Exception("a code string was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("the 'width' keyword was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("the 'scan' keyword was expected!\n${_currentToken.error()}")
        }
    }

    private fun symbol_list(): MutableList<Symbol> {
        var symbol = symbol()
        var symbols = symbol_list_prime()
        symbols.add(0,symbol)
        return symbols
    }

    private fun symbol(): Symbol {
        if(_currentToken.IsTerminal()){
            _currentToken=lexer.nextToken()
            return opt_type_id()
        }else if(_currentToken.IsNon()){
            _currentToken=lexer.nextToken()
            if(_currentToken.IsTerminal()){
                _currentToken=lexer.nextToken()
                return non_terminal_opt_type_id()
            }else{
                throw Exception("the 'terminal' keyword was expected!\n${_currentToken.error()}")
            }
        }else if(_currentToken.IsNonTerminal()){
            _currentToken=lexer.nextToken()
            return non_terminal_opt_type_id()
        }else{
            throw Exception("a symbol was expected!\n${_currentToken.error()}")
        }
    }





    private fun new_non_term_id(): Id {
        if(_currentToken.IsId()){
            var lexeme=_currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun non_term_name_list():MutableList<Id> {
        if(_currentToken.IsComma()){
            _currentToken=lexer.nextToken()
            var nonTerm = new_non_term_id()
            var nonTerms =  non_term_name_list()
            nonTerms.add(0,nonTerm)
            return nonTerms
        }
        //empty
        return mutableListOf()
    }

    private fun opt_type_id(): Terminal {
        if(_currentToken.IsId()){
            var id=type_id()
            if(_currentToken.IsId()){
                var term = new_term_id()
                var terms = term_name_list()
                terms.add(0,term)
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return Terminal(id, terms)
                }else{
                    throw Exception("a ; was expected!\n${_currentToken.error()}")
                }
            }else if(_currentToken.IsComma()){
                var terms = term_name_list()
                terms.add(0,id)
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return Terminal(terms)
                }else{
                    throw Exception("a ; was expected!\n${_currentToken.error()}")
                }
            }else  if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
                return Terminal(listOf(id))
            }else{
                throw Exception("terms declaration was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun non_terminal_opt_type_id(): NonTerminal {
        if(_currentToken.IsId()){
            var id=type_id()
            if(_currentToken.IsId()){
                var nonTerm = new_non_term_id()
                var nonTerms = non_term_name_list()
                nonTerms.add(0,nonTerm)
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return NonTerminal(id, nonTerms)
                }else{
                    throw Exception("a ; was expected!\n${_currentToken.error()}")
                }
            }else if(_currentToken.IsComma()){
                var nonTerms = non_term_name_list()
                nonTerms.add(0,id)
                if(_currentToken.IsSemi()){
                    _currentToken=lexer.nextToken()
                    return NonTerminal(nonTerms)
                }else{
                    throw Exception("a ; was expected!\n${_currentToken.error()}")
                }
            }else  if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
                return NonTerminal(listOf(id))
            }else{
                throw Exception("terms declaration was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun type_id(): Id {
        return multipart_id()
    }

    private fun multipart_id(): Id {
        if(_currentToken.IsId()){
            var lexem = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return multipart_id_prime(lexem)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun multipart_id_prime(lexeme: String) : Id {
        if(_currentToken.IsDot()){
            var dot = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            if(_currentToken.IsId()){
                var id = _currentToken.lexeme
                _currentToken=lexer.nextToken()
                return multipart_id_prime("$lexeme$dot$id")
            }else{
                throw Exception("an id was expected!\n${_currentToken.error()}")
            }
        }
        return Id(lexeme)
        //empty
    }



    private fun new_term_id(): Id {
        if(_currentToken.IsId()){
            var lexeme=_currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun term_name_list() : MutableList<Id> {
        if(_currentToken.IsComma()){
            _currentToken=lexer.nextToken()
            var term = new_term_id()
            var terms = term_name_list()
            terms.add(0,term)
            return terms
        }
        //empty
        return mutableListOf()
    }

    private fun symbol_list_prime() : MutableList<Symbol> {
        if(_currentToken.IsSymbol()){
            var symbol = symbol()
            var symbols = symbol_list_prime()
            symbols.add(0,symbol)
            return symbols
        }
        //empty
        return mutableListOf()
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
                    throw Exception("a ; was expected!\n${_currentToken.error()}")
                }
            }else{
                throw Exception("the 'with' keyword was expected!\n${_currentToken.error()}")
            }
        }
        //empty
    }

    private fun nt_id(): Id {
        if(_currentToken.IsId()){
            var lexeme=_currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun production_list(): MutableList<Production> {
        var production = production()
        var productionList = production_list_prime()
        productionList.add(0,production)
        return productionList
    }

    private fun production() : Production {
        var id = nt_id()
        if(_currentToken.IsColonColonEquals()){
            _currentToken=lexer.nextToken()
            var rhsList = rhs_list()
            if(_currentToken.IsSemi()){
                _currentToken=lexer.nextToken()
                return Production(id.lexeme, rhsList)
            }else{
                throw Exception("a ; was expected!\n${_currentToken.error()}")
            }
        }else{
            throw Exception("a ::= was expected!\n${_currentToken.error()}")
        }
    }

    private fun rhs_list(): MutableList<RightHandSide> {
        var rhs = rhs()
        var rhsList = rhs_list_prime()
        rhsList.add(0,rhs)
        return rhsList
    }

    private fun rhs(): RightHandSide {
        return RightHandSide(prod_part_list())
    }

    private fun prod_part_list() : MutableList<ProdPart> {
        if(_currentToken.IsProdPart()){
            var prodPart = prod_part()
            var prodPartList = prod_part_list()
            prodPartList.add(0,prodPart)
            return prodPartList
        }
        //empty
        return mutableListOf()
    }

    private fun prod_part(): ProdPart {
        if(_currentToken.IsId()){
            var id = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            var label = opt_label()?.lexeme
            return ProdSymbol(id, label)
        }else if(_currentToken.IsCodeString()){
            var codeString = _currentToken.lexeme
            _currentToken=lexer.nextToken()
            return SyntaxDirectedTranslation(codeString)
        }else{
            throw Exception("a production part was expected!\n${_currentToken.error()}")
        }
    }

    private fun opt_label(): Id? {
        if(_currentToken.IsColon()){
            _currentToken=lexer.nextToken()
            return label_id()
        }
        //empty
        return null
    }

    private fun label_id(): Id {
        if(_currentToken.IsId()){
            var lexeme=_currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun opt_term_id() {
        if(_currentToken.IsId()){
            term_id()
        }
        //empty
    }

    private fun term_id(): Id {
        return symbol_id()
    }

    private fun symbol_id(): Id {
        if(_currentToken.IsId()){
            var lexeme=_currentToken.lexeme
            _currentToken=lexer.nextToken()
            return Id(lexeme)
        }else{
            throw Exception("an id was expected!\n${_currentToken.error()}")
        }
    }

    private fun rhs_list_prime() : MutableList<RightHandSide> {
        if(_currentToken.IsBar()){
            _currentToken=lexer.nextToken()
            var rhs = rhs()
            var rhsList = rhs_list_prime()
            rhsList.add(0,rhs)
            return rhsList
        }
        //empty
        return mutableListOf()
    }

    private fun production_list_prime(): MutableList<Production> {
        if(_currentToken.IsId()){
            var production = production()
            var productionList = production_list_prime()
            productionList.add(0,production)
            return productionList
        }
        //empty
        return mutableListOf()
    }


}

