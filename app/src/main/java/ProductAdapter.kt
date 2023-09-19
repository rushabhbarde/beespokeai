import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hrushabhb.beespokeai.databinding.ItemProductBinding

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.addtocart.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val product = productList[position]
                    itemClickListener?.onItemClick(product)
                }
            }
        }

        fun bind(product: Product) {
            binding.apply {
                titleTextView.text = product.title
                priceTextView.text = product.price
                categoryTextView.text = product.category
                descriptionTextView.text = product.description

                Glide.with(itemView)
                    .load(product.imageUrl)
                    .into(productImageView)

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

}