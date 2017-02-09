package Mug

class NonTerminal : Symbol {
    constructor(type: Id, nonTerms: List<Id>):super(type,nonTerms)
    constructor(nonTerms: List<Id>):super(nonTerms)
}