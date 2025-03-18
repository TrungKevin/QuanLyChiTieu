package com.trungkien.qlychitieu2.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.trungkien.qlychitieu2.AppDatabase
import com.trungkien.qlychitieu2.ChiTietThuChi
import com.trungkien.qlychitieu2.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FirstFragment : Fragment() {


    private lateinit var edtDate: EditText
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // Lấy tham chiếu đến các EditText và Button
        val editSoTien = view.findViewById<EditText>(R.id.edit_sotien_frag1)
        val editLyDo = view.findViewById<EditText>(R.id.edit_lydo_frag1)
        val btnLuu = view.findViewById<Button>(R.id.btn_luu_frag1)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        edtDate = view.findViewById<EditText>(R.id.edt_date_frag1)

        // Thiết lập dữ liệu cho Spinner
        val items = listOf("Thu", "Chi")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Xử lý sự kiện khi người dùng chọn một mục từ Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                // Có thể thực hiện các hành động khác dựa trên lựa chọn của người dùng
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Không làm gì
            }
        }

        // Xử lý sự kiện khi người dùng nhấp vào EditText ngày
        edtDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Bắt sự kiện click cho nút Lưu
        btnLuu.setOnClickListener {
            val soTien = editSoTien.text.toString().trim()
            val lyDo = editLyDo.text.toString().trim()
            val date = edtDate.text.toString().trim()
            val selectedItem = spinner.selectedItem as String

            if (soTien.isEmpty() || lyDo.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập dữ liệu", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
            // Kiểm tra xem soTienText có phải là số hợp lệ không
            val soTienText = try {
                soTien.toDouble()
            } catch (e: NumberFormatException) {
                // Nếu không phải số, hiển thị thông báo lỗi
                Toast.makeText(requireContext(), "Số tiền không hợp lệ. Vui lòng nhập một số.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
                val chiTietThuChi = ChiTietThuChi(
                    userID = 1, // Giả sử userID là 1
                    tenThuChi = selectedItem,
                    lyDoThuChi = lyDo,
                    date = date,
                    soTien = soTienText
                )

                // Sử dụng lifecycleScope để chạy coroutine
                lifecycleScope.launch {
                    // Thêm chiTietThuChi vào cơ sở dữ liệu
                    val database = AppDatabase.getDatabase(requireContext())
                    database.chiTietThuChiDAO().insert(chiTietThuChi)

                    // Hiển thị thông báo sau khi lưu thành công
                    Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT).show()
                }

            }
        return view
    }
    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Cập nhật ngày được chọn vào EditText
                calendar.set(selectedYear, selectedMonth, selectedDay)
                updateDateInEditText()
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun updateDateInEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDate = dateFormat.format(calendar.time)
        edtDate.setText(selectedDate)
    }
}