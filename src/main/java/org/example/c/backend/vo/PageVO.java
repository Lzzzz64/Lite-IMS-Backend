package org.example.c.backend.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import java.util.List;

@Data
public class PageVO<T> {
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private long pages;

    // 静态转换方法，放在PageVO里面，service直接调用
    public static <T, E> PageVO<T> convert(IPage<E> iPage, List<T> voList) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setRecords(voList);
        pageVO.setTotal(iPage.getTotal());
        pageVO.setSize(iPage.getSize());
        pageVO.setCurrent(iPage.getCurrent());
        pageVO.setPages(iPage.getPages());
        return pageVO;
    }
}
