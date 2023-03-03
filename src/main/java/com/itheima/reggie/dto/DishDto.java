package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

	private static final long serialVersionUID = 2120460471969610490L;

	private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
