package com.trungkien.qlychitieu2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChiTietThuChiAdapter : ListAdapter<ChiTietThuChi, ChiTietThuChiAdapter.ChiTietThuChiViewHolder>(DiffCallback()) {

    var onItemClick: ((ChiTietThuChi) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChiTietThuChiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chi_tiet_thu_chi, parent, false)
        return ChiTietThuChiViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChiTietThuChiViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    class ChiTietThuChiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTenThuChi: TextView = itemView.findViewById(R.id.txt_ten_thu_chi)
        private val txtLyDo: TextView = itemView.findViewById(R.id.txt_ly_do)
        private val txtSoTien: TextView = itemView.findViewById(R.id.txt_so_tien)
        private val txtNgay: TextView = itemView.findViewById(R.id.txt_ngay)

        fun bind(chiTietThuChi: ChiTietThuChi) {
            txtTenThuChi.text = chiTietThuChi.tenThuChi
            txtLyDo.text = chiTietThuChi.lyDoThuChi
            txtNgay.text = chiTietThuChi.date

            // Kiểm tra loại giao dịch (Thu hoặc Chi) và hiển thị số tiền tương ứng
            if (chiTietThuChi.tenThuChi == "Chi") {
                txtSoTien.text = "-${formatCurrency(chiTietThuChi.soTien)}" // Thêm dấu trừ
                txtSoTien.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)) // Màu đỏ
            } else {
                txtSoTien.text = "+${formatCurrency(chiTietThuChi.soTien)}" // Thêm dấu cộng
                txtSoTien.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)) // Màu xanh lá
            }
        }

        private fun formatCurrency(amount: Double): String {
            val formatter = java.text.DecimalFormat("#,###")
            return formatter.format(amount)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChiTietThuChi>() {
        override fun areItemsTheSame(oldItem: ChiTietThuChi, newItem: ChiTietThuChi): Boolean {
            return oldItem.thuChiID == newItem.thuChiID
        }

        override fun areContentsTheSame(oldItem: ChiTietThuChi, newItem: ChiTietThuChi): Boolean {
            return oldItem == newItem
        }
    }
}