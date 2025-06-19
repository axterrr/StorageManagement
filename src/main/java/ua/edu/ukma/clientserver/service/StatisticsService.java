package ua.edu.ukma.clientserver.service;

public interface StatisticsService {

    Double getTotalProductsPriceInStorage();

    Double getTotalProductsPriceInGroup(Integer groupId);
}
