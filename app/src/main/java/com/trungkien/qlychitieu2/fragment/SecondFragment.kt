package com.trungkien.qlychitieu2.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trungkien.qlychitieu2.AppDatabase
import com.trungkien.qlychitieu2.ChiTietThuChi
import com.trungkien.qlychitieu2.ChiTietThuChiAdapter
import com.trungkien.qlychitieu2.R
import com.trungkien.qlychitieu2.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondFragment : Fragment() {

    private lateinit var btnCalendar: ImageView
    private lateinit var selectedDateRange: EditText
    private lateinit var edtTienThu: EditText
    private lateinit var edtTienChi: EditText
    private lateinit var edtTienSoDu: EditText
    private val calendar = Calendar.getInstance()

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    private lateinit var adapter: ChiTietThuChiAdapter

    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Khởi tạo SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_second, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_list)
        adapter = ChiTietThuChiAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Lấy tham chiếu đến các view
        btnCalendar = view.findViewById(R.id.btn_calendar)
        selectedDateRange = view.findViewById(R.id.selected_date_range)
        edtTienThu = view.findViewById(R.id.edt_tienthu)
        edtTienChi = view.findViewById(R.id.edt_tienchi)
        edtTienSoDu = view.findViewById(R.id.edt_tiensodu)

        // Lấy dữ liệu từ cơ sở dữ liệu
        val database = AppDatabase.getDatabase(requireContext())
        val chiTietThuChiLiveData = database.chiTietThuChiDAO().getAll()

        adapter.onItemClick = { chiTietThuChi ->
            val bundle = Bundle().apply {
                putParcelable("chiTietThuChi", chiTietThuChi)
            }

            // Sử dụng NavController để điều hướng
            findNavController().navigate(R.id.detail_fragment, bundle)
        }


        // Quan sát LiveData và cập nhật RecyclerView khi dữ liệu thay đổi
        chiTietThuChiLiveData.observe(viewLifecycleOwner, Observer { chiTietThuChiList ->
            chiTietThuChiList?.let {
                adapter.submitList(it)
                updateTotals(it) // Cập nhật tổng thu, tổng chi, và số dư
            }
        })

        // Xử lý khi nhấn vào biểu tượng Calendar hoặc EditText
        btnCalendar.setOnClickListener { showStartDatePickerDialog() }
        selectedDateRange.setOnClickListener { showStartDatePickerDialog() }

        return view
    }

    private fun showStartDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            startDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            showEndDatePickerDialog() // Sau khi chọn ngày bắt đầu, mở tiếp DatePicker để chọn ngày kết thúc
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showEndDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            endDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            updateDateRange()
            filterDataByDateRange() // Lọc dữ liệu theo khoảng thời gian
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateDateRange() {
        if (startDate != null && endDate != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateStr = dateFormat.format(startDate!!.time)
            val endDateStr = dateFormat.format(endDate!!.time)

            // Hiển thị khoảng thời gian trên EditText
            selectedDateRange.setText("$startDateStr - $endDateStr")
        }
    }

    private fun filterDataByDateRange() {
        val database = AppDatabase.getDatabase(requireContext())
        val chiTietThuChiLiveData = database.chiTietThuChiDAO().getAll()

        chiTietThuChiLiveData.observe(viewLifecycleOwner, Observer { chiTietThuChiList ->
            chiTietThuChiList?.let {
                val filteredList = it.filter { item ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val itemDate = dateFormat.parse(item.date)
                    itemDate != null && itemDate in startDate!!.time..endDate!!.time
                }
                adapter.submitList(filteredList)
                updateTotals(filteredList) // Cập nhật tổng thu, tổng chi, và số dư
            }
        })
    }

    private fun updateTotals(chiTietThuChiList: List<ChiTietThuChi>) {
        var totalThu = 0.0
        var totalChi = 0.0

        for (item in chiTietThuChiList) {
            if (item.tenThuChi == "Thu") {
                totalThu += item.soTien
            } else if (item.tenThuChi == "Chi") {
                totalChi += item.soTien
            }
        }

        // Hiển thị tổng thu
        edtTienThu.setText("+${formatCurrency(totalThu)}")
        edtTienThu.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))

        // Hiển thị tổng chi
        edtTienChi.setText("-${formatCurrency(totalChi)}")
        edtTienChi.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))

        // Tính số dư
        val soDu = totalThu - totalChi
        // Cập nhật số dư vào SharedViewModel
        sharedViewModel.updateSoDu(soDu)

        if (soDu >= 0) {
            edtTienSoDu.setText("+${formatCurrency(soDu)}")
            edtTienSoDu.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))
        } else {
            edtTienSoDu.setText("${formatCurrency(soDu)}")
            edtTienSoDu.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
        }
    }

    private fun formatCurrency(amount: Double): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(amount)
    }
}