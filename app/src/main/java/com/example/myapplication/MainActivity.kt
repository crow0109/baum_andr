package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LibraryAdapter
    private val libraryItems = mutableListOf<LibraryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLibraryItems()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LibraryAdapter(libraryItems) { item ->
            Toast.makeText(this, "Элемент с id ${item.id}", Toast.LENGTH_SHORT).show()
            item.available = !item.available
            adapter.notifyItemChanged(libraryItems.indexOf(item))
        }
        recyclerView.adapter = adapter
    }

    private fun initLibraryItems() {
        libraryItems.addAll(listOf(
            Book(10000, true, "Искусство любить", 222, "Эрих Фромм"),
            Book(10001, true, "Книга чая", 288, "Какудзо Окакура"),
            Newspaper(10002, true, "Сельская жизнь", "Январь", 794),
            Newspaper(10003, false, "Бауманец", "Февраль", 123),
            Disc(10004, true, "Babymonster - Drip", "CD"),
            Disc(10005, false, "Aespa - Armageddon", "CD")
        ))
    }
}

class LibraryAdapter(
    private val items: List<LibraryItem>,
    private val onItemClick: (LibraryItem) -> Unit
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
        val id: TextView = view.findViewById(R.id.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val item = items[position]

        val iconRes = when (item) {
            is Book -> R.drawable.ic_book
            is Newspaper -> R.drawable.ic_newspaper
            is Disc -> R.drawable.ic_disc
            else -> TODO()
        }
        holder.icon.setImageResource(iconRes)
        holder.name.text = item.name
        holder.id.text = "ID: ${item.id} (${item::class.simpleName})"

        if (item.available) {
            holder.name.alpha = 1F
            holder.id.alpha = 1F
            holder.cardView.cardElevation = 10F.dpToPx(holder.cardView.context)
        } else {
            holder.name.alpha = 0.3F
            holder.id.alpha = 0.3F
            holder.cardView.cardElevation = 1F.dpToPx(holder.cardView.context)
        }

        holder.cardView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}

fun Float.dpToPx(context: android.content.Context): Float {
    return this * context.resources.displayMetrics.density
}