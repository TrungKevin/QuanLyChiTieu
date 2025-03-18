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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.trungkien.qlychitieu2.AppDatabase
import com.trungkien.qlychitieu2.ChiTietThuChi
import com.trungkien.qlychitieu2.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailFragment : Fragment() {

    private lateinit var chiTietThuChi: ChiTietThuChi
    private lateinit var spinnerDetail: Spinner
    private lateinit var editSoTien: EditText
    private lateinit var editLyDo: EditText
    private lateinit var editDate: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private val calendar = Calendar.getInstance() // Thêm biến calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_item, container, false)

        chiTietThuChi = arguments?.getParcelable("chiTietThuChi") ?: throw IllegalStateException("ChiTietThuChi is required")

        spinnerDetail = view.findViewById(R.id.spinner_detail)
        editSoTien = view.findViewById(R.id.edit_sotien_detail)
        editLyDo = view.findViewById(R.id.edit_lydo_detail)
        editDate = view.findViewById(R.id.edt_date_detail)
        btnUpdate = view.findViewById(R.id.btn_Cnhat_detail)
        btnDelete = view.findViewById(R.id.btn_xoa_detail)

        // Thiết lập dữ liệu cho Spinner
        val items = listOf("Thu", "Chi")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDetail.adapter = adapter

        // Xử lý sự kiện khi người dùng chọn một mục từ Spinner
        spinnerDetail.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                // Có thể thực hiện các hành động khác dựa trên lựa chọn của người dùng
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Không làm gì
            }
        }

        // Xử lý sự kiện khi người dùng nhấp vào EditText ngày
        editDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set initial data
        spinnerDetail.setSelection(if (chiTietThuChi.tenThuChi == "Thu") 0 else 1)
        editSoTien.setText(chiTietThuChi.soTien.toString())
        editLyDo.setText(chiTietThuChi.lyDoThuChi)
        editDate.setText(chiTietThuChi.date)

        btnUpdate.setOnClickListener {
            updateChiTietThuChi()
        }

        btnDelete.setOnClickListener {
            deleteChiTietThuChi()
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
        editDate.setText(selectedDate)
    }

    private fun updateChiTietThuChi() {
        // Sử dụng toán tử an toàn và giá trị mặc định
        val tenThuChi = spinnerDetail.selectedItem?.toString() ?: "Thu" // Giá trị mặc định là "Thu"
        val soTienText = editSoTien.text?.toString() ?: "0" // Giá trị mặc định là "0"
        val lyDo = editLyDo.text?.toString() ?: "" // Giá trị mặc định là chuỗi rỗng
        val date = editDate.text?.toString() ?: "" // Giá trị mặc định là chuỗi rỗng

        // Chuyển đổi soTienText thành Double
        val soTien = try {
            soTienText.toDouble()
        } catch (e: NumberFormatException) {
            0.0 // Giá trị mặc định nếu không thể chuyển đổi
        }

        // Tạo đối tượng ChiTietThuChi mới
        val updatedChiTietThuChi = chiTietThuChi.copy(
            tenThuChi = tenThuChi,
            soTien = soTien,
            lyDoThuChi = lyDo,
            date = date
        )

        // Cập nhật dữ liệu trong cơ sở dữ liệu
        lifecycleScope.launch {
            try {
                val database = AppDatabase.getDatabase(requireContext())
                database.chiTietThuChiDAO().update(updatedChiTietThuChi)

                // Quay lại Fragment trước đó
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                // Xử lý lỗi (ví dụ: hiển thị thông báo lỗi)
                e.printStackTrace()
            }
        }
    }

    private fun deleteChiTietThuChi() {
        lifecycleScope.launch {
            try {
                val database = AppDatabase.getDatabase(requireContext())
                database.chiTietThuChiDAO().delete(chiTietThuChi)

                // Quay lại Fragment trước đó
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                // Xử lý lỗi (ví dụ: hiển thị thông báo lỗi)
                e.printStackTrace()
            }
        }
    }
}