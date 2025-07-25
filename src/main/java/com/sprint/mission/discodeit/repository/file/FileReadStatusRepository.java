package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.FileAccessException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(
        prefix="discodeit.repository",
        name="type",
        havingValue="file"
)
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path filePath;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory}/ReadStatus.ser") String filePathStr
    ) {
        this.filePath = Paths.get(filePathStr);
        try {
            Files.createDirectories(this.filePath.getParent());
            if (Files.notExists(this.filePath)) {
                writeToFile(new HashMap<>());
            }
        } catch (IOException e) {
            // 트랜잭션시 롤백을 고려해서 RuntimeException을 상속받은 FileAccessException 형태로 예외 전환해서 던지도록 설정했습니다.
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        try {
            Map<UUID, ReadStatus> all = readFromFile();
            all.put(readStatus.getId(), readStatus);
            writeToFile(all);
            return readStatus;
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new FileAccessException(ErrorCode.FILE_CLASS_NOT_FOUND);
        }
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        try {
            ReadStatus rs = readFromFile().get(id);
            return Optional.ofNullable(rs);
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new FileAccessException(ErrorCode.FILE_CLASS_NOT_FOUND);
        }
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
        try {
            return readFromFile().containsKey(id);
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new FileAccessException(ErrorCode.FILE_CLASS_NOT_FOUND);
        }
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
        try {
            Map<UUID, ReadStatus> all = readFromFile();
            if (all.remove(id) != null) {
                writeToFile(all);
            }
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new FileAccessException(ErrorCode.FILE_CLASS_NOT_FOUND);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        for (ReadStatus rs : findAllByChannelId(channelId)) {
            deleteById(rs.getId());
        }
    }

    private List<ReadStatus> findAll() {
        try {
            return new ArrayList<>(readFromFile().values());
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        } catch (ClassNotFoundException e) {
            throw new FileAccessException(ErrorCode.FILE_CLASS_NOT_FOUND);
        }
    }

    /**
     * 직렬화
     */
    private void writeToFile(Map<UUID, ReadStatus> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(filePath))) {
            oos.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write ReadStatus file", e);
        }
    }

    /**
     * 역직렬화
     */
    private Map<UUID, ReadStatus> readFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(filePath))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (EOFException eof) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }
    }
}
