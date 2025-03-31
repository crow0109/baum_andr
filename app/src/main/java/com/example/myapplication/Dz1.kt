package com.example.myapplication

fun main() {
    val books = listOf(
        Book(10000, true, "Искусство любить", 222, "Эрих Фромм"),
        Book(10001, true, "Книга чая", 288, "Какудзо Окакура")
    )
    val newspapers = listOf(
        Newspaper(10002, true, "Сельская жизнь", 794),
        Newspaper(10003, true, "Бауманец", 123)
    )
    val discs = listOf(
        Disc(10004, true, "Babymonster - Drip", "CD"),
        Disc(10005, true, "Aespa - Armageddon", "CD")
    )

    while (true) {
        println("Выберите тип объекта:")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("4. Выйти")
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> handleItems(books)
            2 -> handleItems(newspapers)
            3 -> handleItems(discs)
            4 -> return
            else -> println("Неверный выбор, попробуйте снова.")
        }
    }
}


abstract class LibraryItem(
    val id: Int,
    var available: Boolean,
    val name: String) {
    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

class Book(
    id: Int,
    available: Boolean,
    name: String,
    val pageCount: Int,
    val author: String) : LibraryItem(id, available, name) {
    override fun getShortInfo(): String {
        return "$name доступна: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "Книга: $name ($pageCount стр.) автора: $author с id: $id доступна: ${if (available) "Да" else "Нет"}"
    }
}

class Newspaper(
    id: Int,
    available: Boolean,
    name: String,
    val issueNumber: Int) : LibraryItem(id, available, name) {
    override fun getShortInfo(): String {
        return "$name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "выпуск: $issueNumber газеты $name с id: $id доступен: ${if (available) "Да" else "Нет"}"
    }
}

class Disc(
    id: Int,
    available: Boolean,
    name: String,
    val type: String) : LibraryItem(id, available, name) {
    override fun getShortInfo(): String {
        return "$type $name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "$type $name доступен: ${if (available) "Да" else "Нет"}"
    }
}

fun handleItems(items: List<LibraryItem>) {
    items.forEachIndexed { index, item ->
        println("${index + 1}. ${item.getShortInfo()}")
    }
    println("Выберите объект (или введите 0 для возврата):")
    val choice = readlnOrNull()?.toIntOrNull() ?: 0
    if (choice == 0) return
    val selectedItem = items.getOrNull(choice - 1) ?: return println("Неверный выбор.")
    println("1. Взять домой")
    println("2. Читать в читальном зале")
    println("3. Показать подробную информацию")
    println("4. Вернуть")
    println("5. Вернуться к выбору типа объекта")
    when (readlnOrNull()?.toIntOrNull()) {
        1 -> takeHome(selectedItem)
        2 -> readInLib(selectedItem)
        3 -> println(selectedItem.getDetailedInfo())
        4 -> returnItem(selectedItem)
        5 -> return
        else -> println("Неверный выбор.")
    }
}

fun takeHome(item: LibraryItem) {
    if (item is Newspaper) {
        println("Газеты нельзя брать домой.")
        return
    }
    if (!item.available) {
        println("Объект недоступен. Поезд ушел.")
        return
    }
    item.available = false
    println("${item::class.simpleName} ${item.id} взяли домой.")
}

fun readInLib(item: LibraryItem) {
    if (item is Disc) {
        println("Диски нельзя тут читать.")
        return
    }
    if (!item.available) {
        println("Объект недоступен. Поезд ушел.")
        return
    }
    item.available = false
    println("${item::class.simpleName} ${item.id} взяли в читальный зал.")
}

fun returnItem(item: LibraryItem) {
    if (item.available) {
        println("Объект уже возвращен.")
        return
    }
    item.available = true
    println("${item::class.simpleName} ${item.id} вернули.")
}