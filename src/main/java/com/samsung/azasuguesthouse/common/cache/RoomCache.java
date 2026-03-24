package com.samsung.azasuguesthouse.common.cache;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomCache {

    private final Map<Long, List<LocalDate>> cache = new ConcurrentHashMap<>();

}
