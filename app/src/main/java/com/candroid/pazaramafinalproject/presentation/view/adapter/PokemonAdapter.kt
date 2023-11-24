import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.candroid.pazaramafinalproject.data.models.PokedexListEntry
import com.candroid.pazaramafinalproject.databinding.PokemonItemBinding
import com.candroid.pazaramafinalproject.util.downloadUrl
import com.candroid.pazaramafinalproject.util.placeHolderProgressBar

class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {

    inner class PokemonHolder(val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var pokedexList: List<PokedexListEntry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {
        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonHolder(binding)
    }

    override fun getItemCount(): Int = pokedexList.size

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val pokedex = pokedexList[position]
        holder.binding.pokemonId.text = pokedex.number.toString()
        holder.binding.pokemonName.text = pokedex.pokemonName
        holder.binding.pokemonImageView.downloadUrl(
            pokedex.imageUrl,
            placeHolderProgressBar(holder.binding.root.context)
        )
    }

    // DiffUtil kullanarak veri setini güncelleyen fonksiyon
    fun updatePokedexList(newPokedexList: List<PokedexListEntry>) {
        val diffCallback = PokemonDiffCallback(pokedexList, newPokedexList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        pokedexList = newPokedexList
        diffResult.dispatchUpdatesTo(this)
    }

    // ViewHolder ve diğer metotlar

    // DiffUtil.Callback sınıfı
    class PokemonDiffCallback(private val oldList: List<PokedexListEntry>, private val newList: List<PokedexListEntry>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].number == newList[newItemPosition].number
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
