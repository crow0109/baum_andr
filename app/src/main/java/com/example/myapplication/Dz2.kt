package com.example.myapplication

fun main() {
    val libraryItems = listOf(
        Book(10000, true, "Искусство любить", 222, "Эрих Фромм"),
        Book(10001, true, "Книга чая", 288, "Какудзо Окакура"),
        Newspaper(10002, true, "Сельская жизнь", 794),
        Newspaper(10003, true, "Бауманец", 123),
        Disc(10004, true, "Babymonster - Drip", "CD"),
        Disc(10005, true, "Aespa - Armageddon", "CD")
    )

    while (true) {
        println("Выберите тип объекта:")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("4. Показать все объекты")
        println("5. Выйти")
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> handleItems(libraryItems.filterIsInstance<Book>())
            2 -> handleItems(libraryItems.filterIsInstance<Newspaper>())
            3 -> handleItems(libraryItems.filterIsInstance<Disc>())
            4 -> handleItems(libraryItems)
            5 -> return
            else -> println("Неверный выбор, попробуйте снова.")
        }
    }
}

abstract class LibraryItem(
    val id: Int,
    var available: Boolean,
    val name: String
) {
    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

interface TakeHome {
    fun takeHome()
}

interface ReadInLibrary {
    fun readInLibrary()
}

class Book(
    id: Int,
    available: Boolean,
    name: String,
    val pageCount: Int,
    val author: String
) : LibraryItem(id, available, name), TakeHome, ReadInLibrary {
    override fun getShortInfo(): String {
        return "$name доступна: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "Книга: $name ($pageCount стр.) автора: $author с id: $id доступна: ${if (available) "Да" else "Нет"}"
    }

    override fun takeHome() {
        if (!available) {
            println("Объект недоступен. Поезд ушел.")
            return
        }
        available = false
        println("Книга $id взята домой.")
    }

    override fun readInLibrary() {
        if (!available) {
            println("Объект недоступен. Поезд ушел.")
            return
        }
        available = false
        println("Книга $id взята в читальный зал.")
    }
}

class Newspaper(
    id: Int,
    available: Boolean,
    name: String,
    val issueNumber: Int
) : LibraryItem(id, available, name), ReadInLibrary {
    override fun getShortInfo(): String {
        return "$name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "Выпуск: $issueNumber газеты $name с id: $id доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun readInLibrary() {
        if (!available) {
            println("Объект недоступен. Поезд ушел.")
            return
        }
        available = false
        println("Газета $id взята в читальный зал.")
    }
}

class Disc(
    id: Int,
    available: Boolean,
    name: String,
    val type: String
) : LibraryItem(id, available, name), TakeHome {
    override fun getShortInfo(): String {
        return "$type $name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "$type $name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun takeHome() {
        if (!available) {
            println("Объект недоступен. Поезд ушел.")
            return
        }
        available = false
        println("Диск $id взят домой.")
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
        1 -> {
            if (selectedItem is TakeHome) {
                selectedItem.takeHome()
            } else {
                println("Этот объект нельзя взять домой.")
            }
        }

        2 -> {
            if (selectedItem is ReadInLibrary) {
                selectedItem.readInLibrary()
            } else {
                println("Этот объект нельзя читать в зале.")
            }
        }

        3 -> println(selectedItem.getDetailedInfo())
        4 -> returnItem(selectedItem)
        5 -> return
        else -> println("Неверный выбор.")
    }
}

fun returnItem(item: LibraryItem) {
    if (item.available) {
        println("Объект уже возвращен.")
        return
    }
    item.available = true
    println("${item::class.simpleName} ${item.id} вернули.")
}