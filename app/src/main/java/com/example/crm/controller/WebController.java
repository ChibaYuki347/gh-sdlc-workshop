package com.example.crm.controller;

import com.example.crm.model.Customer;
import com.example.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * 画面表示用コントローラ
 * Thymeleafテンプレートを使用したMVCコントローラ
 */
@Controller
public class WebController {

    @Autowired
    private CustomerService customerService;

    // ダッシュボード
    @GetMapping("/")
    public String dashboard(Model model) {
        Map<String, Object> report = customerService.generateMonthlyReport();
        model.addAttribute("report", report);
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("recentCustomers", customers.size() > 5 ? customers.subList(0, 5) : customers);
        model.addAttribute("upcomingRenewalsCount", customerService.countUpcomingRenewals(30));
        return "dashboard";
    }

    // 顧客一覧
    @GetMapping("/customers")
    public String listCustomers(@RequestParam(required = false) String keyword, Model model) {
        List<Customer> customers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            customers = customerService.searchCustomers(keyword.trim());
            model.addAttribute("keyword", keyword);
        } else {
            customers = customerService.getAllCustomers();
        }
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    // 顧客詳細
    @GetMapping("/customers/{id}")
    public String customerDetail(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "customers/detail";
    }

    // 新規登録フォーム
    @GetMapping("/customers/new")
    public String newCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("editMode", false);
        return "customers/form";
    }

    // 新規登録処理
    @PostMapping("/customers")
    public String createCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        Customer saved = customerService.createCustomer(customer);
        redirectAttributes.addFlashAttribute("message", "顧客を登録しました。");
        return "redirect:/customers/" + saved.id;
    }

    // 編集フォーム
    @GetMapping("/customers/{id}/edit")
    public String editCustomerForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        model.addAttribute("editMode", true);
        return "customers/form";
    }

    // 更新処理
    @PostMapping("/customers/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer,
                                 RedirectAttributes redirectAttributes) {
        customerService.updateCustomer(id, customer);
        redirectAttributes.addFlashAttribute("message", "顧客情報を更新しました。");
        return "redirect:/customers/" + id;
    }

    // 削除確認・実行
    @GetMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable Long id, @RequestParam(required = false) String confirm,
                                 Model model, RedirectAttributes redirectAttributes) {
        if ("yes".equals(confirm)) {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "顧客を削除しました。");
            return "redirect:/customers";
        }
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "customers/delete-confirm";
    }

    // 保険料計算
    @GetMapping("/premium-calculator")
    public String premiumCalculator(
            @RequestParam(required = false) String policyType,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false, defaultValue = "false") boolean isSmoker,
            Model model) {
        if (policyType != null && age != null) {
            double premium = customerService.calculatePremium(policyType, age, isSmoker);
            model.addAttribute("result", premium);
            model.addAttribute("policyType", policyType);
            model.addAttribute("age", age);
            model.addAttribute("isSmoker", isSmoker);
        }
        return "premium-calculator";
    }

    // 月次レポート
    @GetMapping("/report")
    public String monthlyReport(Model model) {
        Map<String, Object> report = customerService.generateMonthlyReport();
        model.addAttribute("report", report);
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "report";
    }

    // 更新通知一覧
    @GetMapping("/renewals")
    public String renewals(Model model) {
        List<Customer> renewals = customerService.getUpcomingRenewals(30);
        model.addAttribute("renewals", renewals);
        return "renewals";
    }
}
