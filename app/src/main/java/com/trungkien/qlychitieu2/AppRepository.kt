package com.trungkien.qlychitieu2

class AppRepository(private val accountDAO: AccountDAO, private val chiTietThuChiDao: ChiTietThuChiDao) {

    suspend fun insertAccount(account: Account) {
        accountDAO.insert(account)
    }

    suspend fun getAccountByID(userID: Int): Account? {
        return accountDAO.getAccountByID(userID)
    }

    suspend fun login(username: String, password: String): Account? {
        return accountDAO.login(username, password)
    }

    suspend fun insertChiTietThuChi(chiTietThuChi: ChiTietThuChi) {
        chiTietThuChiDao.insert(chiTietThuChi)
    }

    suspend fun getChiTietThuChiByUserID(userID: Int): List<ChiTietThuChi> {
        return chiTietThuChiDao.getChiTietThuChiByUserID(userID)
    }

    suspend fun getAccountByUsername(username: String): Account? {
        return accountDAO.getAccountByUsername(username)
    }



}