import java.util.*

object Todos {
    var toDos = HashMap<String, Item>();

    fun getAll(): ArrayList<Item> {
        return java.util.ArrayList(toDos.values)
    }

    fun get(url: String): Item {
        return toDos[url]!!
    }

    fun deleteAll(): ArrayList<Item> {
        toDos.clear()
        return java.util.ArrayList(toDos.values)
    }

    fun delete(url: String): Item {
        return toDos.remove(url)!!
    }

    fun save(url: String, item: Item): Item {
        toDos.put(url, item)
        return item;
    }

    fun update(url: String, item: PatchItem): Item {
        var oldItem = toDos.get(url);

        var title = item.title ?: oldItem!!.title
        var completed = item.completed ?: oldItem!!.completed
        var order = item.order ?: oldItem!!.order

        val newItem = Item(title = title, completed = completed, order = order, url = url)
        toDos.put(url, newItem)
        return newItem
    }
}