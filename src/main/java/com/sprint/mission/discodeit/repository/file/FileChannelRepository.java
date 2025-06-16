package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "./data/channels.ser";
    private final File storageFile;

    public FileChannelRepository() {
        this.storageFile = new File(FILE_PATH);
        if (!storageFile.exists()) {
            writeChannelsToFile(new HashSet<>());
        }
        // TODO: 테스트끝나면 아래 코드 지우기 . 현재는 실행할 때마다 새로 초기화하기 위해서 추가함
        writeChannelsToFile(new HashSet<>());
    }

    /* =========================================================
     * File I/O
     * ========================================================= */

    /*
     * 역직렬화
     * */
    private Set<Channel> readChannelsFromFile() {
        if (!storageFile.exists() || storageFile.length() == 0) {
            return new HashSet<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            return (Set<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 목록 조회 실패", e);
        }
    }

    /*
     * 직렬화
     * */
    private void writeChannelsToFile(Set<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 목록 저장 실패", e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        Set<Channel> allChannels = readChannelsFromFile();
        allChannels.removeIf(c -> c.getId().equals(channel.getId()));
        allChannels.add(channel);
        writeChannelsToFile(allChannels);
        return channel;
    }

    @Override
    public Set<Channel> findAllByRecordStatusIsActive() {
        return readChannelsFromFile().stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Channel> findById(String id) {
        return readChannelsFromFile().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Channel> findByRecordStatusIsActiveId(String id) {
        return readChannelsFromFile().stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> findByChannelName(String channelName) {
        return readChannelsFromFile().stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getChannelName().equals(channelName))
                .toList();
    }

    @Override
    public List<Channel> findByUserId(String userId) {
        return readChannelsFromFile().stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getUsers().stream()
                        .anyMatch(u -> u.getId().equals(userId)))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        Set<Channel> allChannels = readChannelsFromFile();
        Channel deleteChannel = allChannels.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Channel not found"));

        deleteChannel.softDelete();
        deleteChannel.touch();
        writeChannelsToFile(allChannels);
    }

    @Override
    public void restoreById(String id) {
        Set<Channel> allChannels = readChannelsFromFile();
        Channel restoreChannel = allChannels.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Channel not found"));

        restoreChannel.restore();
        restoreChannel.touch();
        writeChannelsToFile(allChannels);
    }

    @Override
    public void deleteById(String id) {
        Set<Channel> allChannels = readChannelsFromFile();
        allChannels.removeIf(c -> c.getId().equals(id));
        writeChannelsToFile(allChannels);
    }
}
