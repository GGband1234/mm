package org.example;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.domain.po.*;
import org.example.domain.vo.UserVo;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class BenUtilsTest {
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;
    @Autowired
    BudgetService budgetService;
    @Autowired
    SuppliersService suppliersService;
    @Test
    public void beanUtilstest(){
        User user = new User();
        UserVo userVo = new UserVo();
        user.setUserId(1);
        user.setEmail("44565");
        user.setPassword("44565");
        user.setPermission(1);
        user.setCreateTime(LocalDateTime.now());
        user.setPhoneNumber("1234558");
        BeanUtils.copyProperties(user,userVo);
        System.out.println(userVo);
        System.out.println(user);
    }
    @Test
    public void getGenkId(){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Integer> ids = new ArrayList<>();
        List<Integer> ids1 = new ArrayList<>();
        userLambdaQueryWrapper.like(User::getUsername,"z");
        List<User> users = userService.list(userLambdaQueryWrapper);
        System.out.println(users);
        for (User user : users) {
            ids.add(user.getUserId());
        }
        System.out.println(ids);
        Page<Projects> page = Page.of(1, 2);

        Page<Projects> page1 = projectService
                .lambdaQuery().in(Projects::getProjectManager, ids)
                .page(page);
        System.out.println(page1.getTotal());
        System.out.println(page1.getPages());
        System.out.println(page1.getRecords());
        List<Projects> records = page1.getRecords();
        for (Projects project : records) {
            ids1.add(project.getProjectId());
        }
        System.out.println(ids1);
        LambdaQueryWrapper<Budgets> userLambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper3.in(Budgets::getProjectId,ids1);
//        List<Budgets> budgets = budgetsMapper.selectList(userLambdaQueryWrapper3);
//        System.out.println(budgets);
    }

    @Test
    public void delectTest(){
        Projects project = projectService.lambdaQuery()
                .eq(Projects::getProjectName, "青青草原6")
                .one();
        System.out.println(project);
//        projectService.removeById(project.getProjectId());
//        QueryWrapper<Budgets> budgetsQueryWrapper = new QueryWrapper<>();
//        budgetsQueryWrapper.eq("project_id",project.getProjectId());
//        budgetService.remove(budgetsQueryWrapper);

    }

    @Test
    public void getBudgetIfoById(){
        BudgetsVo budgetsVo = new BudgetsVo();
        Budgets budget = budgetService.getById(2);
        System.out.println(budget);
        Projects project = projectService.getById(budget.getProjectId());
        User user = userService.getById(project.getProjectManager());
        BeanUtils.copyProperties(budget,budgetsVo);
        BeanUtils.copyProperties(project,budgetsVo);
        BeanUtils.copyProperties(user,budgetsVo);
        System.out.println(budgetsVo);

    }


    @Test
    public void TestPlusGenerate(){
        Suppliers suppliers = new Suppliers();
        suppliers.setUpdateTime(LocalDateTime.now());
        suppliers.setCreateTime(LocalDateTime.now());
        suppliers.setPrice(BigDecimal.valueOf(20));
        suppliers.setUnit("555");

        suppliers.setMaterialName("45654");
        suppliers.setSupplierName("465798");
        suppliers.setEmail("5465545");
        suppliersService.save(suppliers);
        System.out.println(suppliers);
    }

    @Test
    public void TestList(){
        ArrayList<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integers.add(i);
        }
        int i = 0;
        for (Integer integer : integers) {
            System.out.println(integers.get(i));
            i++;
        }
    }

    //测试查询不到是否会报错
    @Test
    public void testselect(){
        System.out.println(userService.getById(6));

    }

    @Test
    public void testBigdecime(){
        BigDecimal sum =new BigDecimal("0");
        BigDecimal s = new BigDecimal(200);
        BigDecimal ss = new BigDecimal(-300);
        sum = sum.add(s);
        sum = sum.add(ss);
        System.out.println(sum);

    }

    @Test
    public void TestHashSet(){
        HashSet<Map<String, Object>> set = new HashSet<>();
        Map<String, Object> map = new HashMap<>();
        map.put("水泥","11");
        map.put("a","22");
        map.put("b","33");
        map.put("c","44");
        Map<String, Object> map1 = new HashMap<>();
        map.put("水泥","111");
        map.put("a","222");
        map.put("b","333");
        map.put("d","444");
        set.add(map);
        set.add(map1);
        System.out.println(set);

    }

    @Test
    public void TestListAndSet(){
        List<Outbound> outbounds = new ArrayList<>();
        Outbound outbound = new Outbound();
        outbound.setMaterialName("水泥");
        outbounds.add(outbound);
        Collection<Outbound> collection = outbounds;
        System.out.println(collection);

    }

    @Test
    public void Teststring(){
        String a = "asdfasfd，asdfasdf,asdfasd";
        String[] split = a.split("。");
        String b = "34";
        for (String s : split) {
            System.out.println(s);

        }
    }

    @Test
    public void pattre(){
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\[\\]\\\\]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher("fadfjlk");
        System.out.println(matcher.find());
    }


}
