package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository() {
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
    private void writeBinaryContentsToFile(BinaryContent binaryContent, Path path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed for " + path, e);
        }
    }

    /**
     * 역직렬화
     */
    private BinaryContent readBinaryContentsFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Deserialization failed for " + path, e);
        }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        writeBinaryContentsToFile(binaryContent, path);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            return Optional.of(readBinaryContentsFromFile(path));
        }
        return Optional.empty();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        Set<UUID> idSet = new HashSet<>(ids);
        return findAll().stream()
                .filter(bc -> idSet.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(this::readBinaryContentsFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list Binary Content files in " + DIRECTORY, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
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
}
