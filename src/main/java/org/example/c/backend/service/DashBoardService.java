package org.example.c.backend.service;

import org.example.c.backend.vo.DashBoardVO;

/**
 * @author 123456
 * @description 针对表【dash_board(仪表盘)】的数据库操作Service
 * @createDate 2026-09-29 10:14:10
 */
public interface DashBoardService {
    /**
     * 获取仪表盘数据
     * @return
     */
    DashBoardVO getDashBoardData();
}
