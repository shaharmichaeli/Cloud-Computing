package demo

class Name {
    var first: String? = null
    var last: String? = null

    constructor() {}
    constructor(first: String?, last: String?) : super() {
        this.first = first
        this.last = last
    }

    override fun toString(): String {
        return "Name [first=$first, last=$last]"
    }
}
