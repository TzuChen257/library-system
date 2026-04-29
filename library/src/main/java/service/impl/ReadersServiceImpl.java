package service.impl;

import dao.ReadersDao;
import dao.impl.ReadersDaoImpl;
import exception.AppException;
import model.Readers;
import service.ReadersService;
import util.Tool;

import java.util.List;
import java.util.regex.Pattern;

public class ReadersServiceImpl implements ReadersService {

    private final ReadersDao readersDao = new ReadersDaoImpl();

    // 商業專案：至少要有基本格式檢查（可再依需求調整）
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");
    @Override
    public Readers loginReader(String username, String password) {
        Readers r = readersDao.findByUsername(username);
        if (r == null) return null;
        if (r.getOnline_password() == null) return null;
        if (!password.equals(r.getOnline_password())) return null;
        return r;
    }

    @Override
    public Readers createReader(Readers r, String rawPassword) {
        if (r == null) throw new AppException("讀者資料不得為空");

        if (r.getOnline_username() == null || !USERNAME.matcher(r.getOnline_username()).matches()) {
            throw new AppException("帳號格式錯誤：4~20碼英數或底線");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new AppException("密碼不得為空");
        }
r.setReader_id(r.getReader_id() == null ? Tool.newReaderId() : r.getReader_id());
        r.setOnline_password(rawPassword);

        readersDao.create(r);
        return r;
    }

    @Override
    public List<Readers> allReaders() {
        return readersDao.findAll();
    }

    @Override
    public int updateReader(Readers r, String rawPasswordOrNull) {
        if (rawPasswordOrNull != null && !rawPasswordOrNull.trim().isEmpty()) {
            r.setOnline_password(rawPasswordOrNull);
        }
        return readersDao.update(r);
    }
}
