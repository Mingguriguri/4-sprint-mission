package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileReadStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    /**
     * 직렬화
     */
    private void writeReadStatusesToFile(ReadStatus readStatus, Path path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed for " + path, e);
        }
    }

    /**
     * 역직렬화
     */
    private ReadStatus readReadStatusesFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (ReadStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Deserialization failed for " + path, e);
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        writeReadStatusesToFile(readStatus, path);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            return Optional.of(readReadStatusesFromFile(path));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ReadStatus> findByUserId(UUID userId) {
        return findAllByUserId(userId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByChannelId(UUID channelId) {
        return findAllByChannelId(channelId)
                .stream()
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll()
                .stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return findAll()
                .stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return !findAllByUserId(userId).isEmpty();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return findAll().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId)
                        && rs.getChannelId().equals(channelId));
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        findAllByChannelId(channelId).stream()
                .map(ReadStatus::getId)
                .forEach(this::deleteById);
    }

    private List<ReadStatus> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(this::readReadStatusesFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list ReadStatus files in " + DIRECTORY, e);
        }
    }
}
