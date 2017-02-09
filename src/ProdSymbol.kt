package Mug

class ProdSymbol : ProdPart {
    private var  id: String
    private var  label: String?

    constructor(id: String, label: String?){
        this.id = id
        this.label=label
    }
}