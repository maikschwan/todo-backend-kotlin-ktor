data class Item( var url: String?, val title: String?, val order: Int?, val completed: Boolean? = false)

data class PatchItem( var url: String?, val title: String?, val order: Int?, val completed: Boolean?)