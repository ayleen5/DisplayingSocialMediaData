package com.tsfn.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.tsfn.model.Action;
import com.tsfn.repository.ActionsRepository;
import com.tsfn.service.exceptions.CouponAlreadyExistsException;
import com.tsfn.service.exceptions.CouponNotAvailableException;
import com.tsfn.service.exceptions.CouponNotFoundException;

@Service
public class ActionService {

	@Autowired
	private ActionsRepository couponRepository;

	public Action getCouponById(int id) {
        return couponRepository.findById(id)
                               .orElseThrow(() -> new CouponNotFoundException("Coupon not found with ID: " + id));
    }

	public Action addAction(Action action) {
		// Check if a coupon with the same title already exists for the same company
//		if (couponRepository.findByTitleAndCompanyId(action.getTitle(), action.getCompanyId()).isPresent()) {
//			throw new CouponAlreadyExistsException("Coupon with the same title already exists for this company");
//		}
		return couponRepository.save(action);
	}

	public Action updateCoupon(Action action) {
		// Check if the coupon exists
		if (!couponRepository.existsById(action.getId())) {
			throw new CouponNotFoundException("Coupon not found.");
		}
		// Update the coupon
		return couponRepository.save(action);
	}

	public void deleteCoupon(int couponId) {
		// Delete the coupon
		if (!couponRepository.existsById(couponId)) {
			throw new CouponNotFoundException("Coupon not found.");
		}
		couponRepository.deleteById(couponId);
	}

	public List<Action> getAllCompanyCoupons(int companyId) {
		return couponRepository.findByCompanyId(companyId);
	}

	public List<Action> getCompanyCouponsByCategory(int companyId, Action category) {
		return couponRepository.findByCompanyIdAndCategory(companyId, category);
	}

	public List<Action> getCompanyCouponsByMaxPrice(int companyId, double maxPrice) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyId, maxPrice);
	}

	public void purchaseCoupon(int customerId, int couponId) {
		// Check if the coupon exists
		Action action = couponRepository.findById(couponId)
				.orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

		// Check if the coupon is still valid for purchase
//		if (action.getEndDate().isBefore(LocalDate.now()) || action.getAmount() <= 0) {
//			throw new CouponNotAvailableException("Coupon is no longer available for purchase");
//		}

	
	}

}
