package Mug

open class Symbol{
    var  type: Id? = null
    var  terms: List<Id>

    constructor(type: Id, terms: List<Id>){
        this.type=type
        this.terms=terms
    }
    constructor(terms: List<Id>){
        this.terms=terms
    }
}