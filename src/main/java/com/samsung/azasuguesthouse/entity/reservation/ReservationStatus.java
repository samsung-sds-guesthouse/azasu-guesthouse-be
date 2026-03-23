package com.samsung.azasuguesthouse.entity.reservation;

public enum ReservationStatus {
    PENDING, // 관리자 승인 대기중
    CONFIRMED, // 예약 확정
    CANCELLED, // 예약 취소
}
