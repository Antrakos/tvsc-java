package com.tvsc.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Season {
    private Integer number;
    private String banner;
    private List<Episode> episodes;
}
