INSERT INTO tb_cohort (id, generation, name, created_at, updated_at)
VALUES
    (1, 10, '10기', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 11, '11기', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_part (id, name, cohort_id, created_at, updated_at)
VALUES
    (1, 'SERVER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'WEB', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'iOS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'ANDROID', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 'DESIGN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 'SERVER', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 'WEB', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 'iOS', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 'ANDROID', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10, 'DESIGN', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_team (id, name, cohort_id, created_at, updated_at)
VALUES
    (1, 'Team A', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Team B', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Team C', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_member (id, login_id, password, name, phone, status, role, created_at, updated_at)
VALUES
    (1, 'admin', '$2y$10$qVuuot2xiZcqF/Wp1r3Hb.epmXzImf7fw1O4FcUcq.IfT5Ogy1z6G', '관리자', '010-0000-0000', 'ACTIVE', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_cohort_member (id, member_id, cohort_id, part_id, team_id, deposit, excuse_count, created_at, updated_at)
VALUES
    (1, 1, 2, NULL, NULL, 100000, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_deposit_history (id, cohort_member_id, type, amount, balance_after, attendance_id, description, created_at, updated_at)
VALUES
    (1, 1, 'INITIAL', 100000, 100000, NULL, '초기 보증금', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
