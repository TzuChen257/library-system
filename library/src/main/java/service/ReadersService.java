package service;

import model.Readers;

public interface ReadersService {
    Readers loginReader(String username, String password);
    Readers createReader(Readers r, String rawPassword);
    java.util.List<Readers> allReaders();
    int updateReader(Readers r, String rawPasswordOrNull);
}
