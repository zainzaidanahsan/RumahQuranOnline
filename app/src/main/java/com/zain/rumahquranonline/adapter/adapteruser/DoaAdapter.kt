import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.databinding.ItemDoaBinding
import com.zain.rumahquranonline.model.modelDoa.DoaResponseItem

class DoaAdapter(
    private val doaList: List<DoaResponseItem>,
    private val onClick: (DoaResponseItem) -> Unit
) : RecyclerView.Adapter<DoaAdapter.DoaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaViewHolder {
        val binding = ItemDoaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoaViewHolder, position: Int) {
        val item = doaList[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int = doaList.size

    class DoaViewHolder(private val binding: ItemDoaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doa: DoaResponseItem, onClick: (DoaResponseItem) -> Unit) {
            binding.tvItemName.text = doa.doa
            binding.root.setOnClickListener { onClick(doa) }
        }
    }
}
