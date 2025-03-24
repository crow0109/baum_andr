package com.example.myapplication

fun main() {
    val libraryItems = mutableListOf(
        Book(10000, true, "Искусство любить", 222, "Эрих Фромм"),
        Book(10001, true, "Книга чая", 288, "Какудзо Окакура"),
        Newspaper(10002, true, "Сельская жизнь", "Январь", 794),
        Newspaper(10003, true, "Бауманец", "Февраль", 123),
        Disc(10004, true, "Babymonster - Drip", "CD"),
        Disc(10005, true, "Aespa - Armageddon", "CD")
    )

    val manager = Manager()
    val digitalizationCabinet = DigitalizationCabinet()

    while (true) {
        println("Выберите действие:")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("4. Показать все объекты")
        println("5. Купить объект")
        println("6. Оцифровать объект")
        println("7. Выйти")
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> handleItems(libraryItems.filterIsInstance<Book>())
            2 -> handleItems(libraryItems.filterIsInstance<Newspaper>())
            3 -> handleItems(libraryItems.filterIsInstance<Disc>())
            4 -> handleItems(libraryItems)
            5 -> {
                println("Выберите:")
                println("1. Послать за книгой")
                println("2. Послать за диском")
                println("3. Послать за газетой")
                when (readlnOrNull()?.toIntOrNull()) {
                    1 -> {
                        val newBook = manager.buy(BookShop())
                        println("Приобретено: ${newBook.name} 1 штука")
                        libraryItems.add(newBook)
                    }

                    2 -> {
                        val newDisc = manager.buy(DiscShop())
                        println("Приобретено: ${newDisc.name} 1 штука")
                        libraryItems.add(newDisc)
                    }

                    3 -> {
                        val newNewspaper = manager.buy(NewspaperShop())
                        println("Приобретено: ${newNewspaper.name} 1 штука")
                        libraryItems.add(newNewspaper)
                    }

                    else -> println("Неверный выбор.")
                }
            }

            6 -> {
                println("Выберите объект для оцифровки:")
                libraryItems.forEachIndexed { index, item ->
                    println("${index + 1}. ${item.getShortInfo()}")
                }
                val choice = readlnOrNull()?.toIntOrNull() ?: 0
                if (choice == 0) return
                val selectedItem =
                    libraryItems.getOrNull(choice - 1) ?: return println("Такого нет")
                when (selectedItem) {
                    is Book -> {
                        val disc = digitalizationCabinet.digitalizeBook(selectedItem)
                        libraryItems.add(disc)
                        println("Оцифрована книга: ${selectedItem.name} в диск: ${disc.name}")
                    }

                    is Newspaper -> {
                        val disc = digitalizationCabinet.digitalizeNewspaper(selectedItem)
                        libraryItems.add(disc)
                        println("Оцифрована газета: ${selectedItem.name} в диск: ${disc.name}")
                    }

                    else -> {
                        println("Этот объект нельзя оцифровать.")
                        continue
                    }
                }
            }

            7 -> return
            else -> {
                println("Неверный выбор, попробуйте снова.")
            }
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
    val month: String,
    val issueNumber: Int
) : LibraryItem(id, available, name), ReadInLibrary {
    override fun getShortInfo(): String {
        return "$name доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "Выпуск: $issueNumber газеты $name, выпущенная в месяце: $month, с id: $id доступен: ${if (available) "Да" else "Нет"}"
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

interface Shop<T : LibraryItem> {
    fun sell(): T
}

class BookShop : Shop<Book> {
    override fun sell(): Book {
        return Book(10006, true, "Магазин шаговой недоступности", 255, "Ким Хоён")
    }
}

class DiscShop : Shop<Disc> {
    override fun sell(): Disc {
        return Disc(10007, true, "ITZY - Gold", "CD")
    }
}

class NewspaperShop : Shop<Newspaper> {
    override fun sell(): Newspaper {
        return Newspaper(10008, true, "Та самая газета", "Cентябрь", 119)
    }
}

class Manager {
    fun <T : LibraryItem> buy(shop: Shop<T>): T {
        return shop.sell()
    }
}

class DigitalizationCabinet {
    fun digitalizeBook(book: Book): Disc {
        return Disc(book.id, true, "Книга ${book.name}, перенесенная на диск", "CD")
    }

    fun digitalizeNewspaper(newspaper: Newspaper): Disc {
        return Disc(newspaper.id, true, "Газета ${newspaper.name}, перенесенная на диск", "CD")
    }
}

inline fun <reified T> functia(items: List<Any>): List<T> {
    return items.filterIsInstance<T>()
}