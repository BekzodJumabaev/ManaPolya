package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.entity.District;
import org.example.entity.Region;
import org.example.repository.DistrictRepository;
import org.example.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final DistrictRepository districtRepository;
    private final RegionRepository regionRepository;

    @Override
    public void run(String... args) throws Exception {

        if (regionRepository.count() == 0) {

            // 1. Toshkent shahri va tumanlari
            saveRegionWithDistricts("Toshkent shahri",
                    List.of("Chilonzor", "Yunusobod", "Yakkasaroy", "Mirzo Ulug'bek", "Sergeli", "Mirobot", "Uchtepa", "Shayxontohur", "Olmazor", "Yashnobod", "Bektemir", "Yangihayot"));

// 2. Toshkent viloyati
            saveRegionWithDistricts("Toshkent viloyati",
                    List.of("Chirchiq", "Angren", "Olmaliq", "Bekobod", "Qibray", "Zangiota", "Yangiyo'l", "Keles", "Parkent", "Bo'stonliq", "Chinoz", "Oqqorg'on", "Piskent"));

// 3. Samarqand viloyati
            saveRegionWithDistricts("Samarqand viloyati",
                    List.of("Samarqand shahri", "Pastdarg'om", "Ishtixon", "Bulung'ur", "Urgut", "Kattaqo'rg'on", "Payariq", "Jomboy", "Narpay", "Oqdaryo", "Toyloq"));

// 4. Farg'ona viloyati
            saveRegionWithDistricts("Farg'ona viloyati",
                    List.of("Farg'ona shahri", "Marg'ilon", "Qo'qon", "Quva", "Rishton", "Oltiariq", "Bog'dod", "Uchko'prik", "Toshloq", "Beshariq", "Yazyavan"));

// 5. Qoraqalpog'iston Respublikasi
            saveRegionWithDistricts("Qoraqalpog'iston Respublikasi",
                    List.of("Nukus shahri", "Qo'ng'irot", "Mo'ynoq", "Beruniy", "Sho'manay", "Shimbay", "To'rtko'l", "Xo'jayli", "Amudaryo", "Ellikqal'a", "Kegeyli"));

// 6. Andijon viloyati
            saveRegionWithDistricts("Andijon viloyati",
                    List.of("Andijon shahri", "Asaka", "Shahrixon", "Xonobod", "Xodjaobod", "Izboskan", "Baliqchi", "Marhamat", "Paxtaobod", "Oltinkul", "Qo'rg'ontepa"));

// 7. Namangan viloyati
            saveRegionWithDistricts("Namangan viloyati",
                    List.of("Namangan shahri", "Chust", "Kosonsoy", "Uychi", "Uchqo'rg'on", "Pop", "Yangiqo'rg'on", "To'raqo'rg'on", "Chortoq", "Mingbuloq", "Norin"));

// 8. Buxoro viloyati
            saveRegionWithDistricts("Buxoro viloyati",
                    List.of("Buxoro shahri", "Gijduvon", "Kogon", "Vobkent", "Qorako'l", "Olot", "Romitan", "Shofirkon", "Peshku", "Qoravulbozor", "Jondor"));

// 9. Navoiy viloyati
            saveRegionWithDistricts("Navoiy viloyati",
                    List.of("Navoiy shahri", "Zarafshon", "Uchkuduk", "Karmana", "Xatirchi", "Qiziltepa", "Nurota", "Kanimex", "Tomdi"));

// 10. Qashqadaryo viloyati
            saveRegionWithDistricts("Qashqadaryo viloyati",
                    List.of("Karshi shahri", "Shahrisabz", "Kitob", "Yakkabog'", "Guzor", "Chiroqchi", "Kamashi", "Koson", "Muborak", "Nishon", "Dehqonobod"));

// 11. Surxondaryo viloyati
            saveRegionWithDistricts("Surxondaryo viloyati",
                    List.of("Termiz shahri", "Denov", "Sherobod", "Jarqo'rg'on", "Sho'rchi", "Boysun", "Qumqo'rg'on", "Sariosiyo", "Uzun", "Muzrabot", "Angor"));

// 12. Jizzax viloyati
            saveRegionWithDistricts("Jizzax viloyati",
                    List.of("Jizzax shahri", "Zomin", "G'allaorol", "Do'stlik", "Paxtakor", "Mirzachul", "Forish", "Zarbdor", "Arnasoy", "Sharaf Rashidov"));

// 13. Sirdaryo viloyati
            saveRegionWithDistricts("Sirdaryo viloyati",
                    List.of("Guliston shahri", "Shirin", "Yangiyer", "Sirdaryo shahri", "Boyovut", "Sayxunobod", "Oqoltin", "Sardoba", "Xovos"));

// 14. Xorazm viloyati
            saveRegionWithDistricts("Xorazm viloyati",
                    List.of("Urganch shahri", "Xiva", "Gurlan", "Shovot", "Xonqa", "Bog'ot", "Qo'shko'pir", "Hazorasp", "Yangiariq", "Yangibozor", "Tuproqqal'a"));

            System.out.println("Barcha hududlar muvaffaqiyatli yuklandi!");
        }
    }

    private void saveRegionWithDistricts(String regionName, List<String> districtName) {
        Region region = new Region();
        region.setRegionName(regionName);
        regionRepository.save(region);

        for (String dName: districtName) {
            District district = new District();
            district.setDistrictName(dName);
            district.setRegion(region);
            districtRepository.save(district);
        }
    }
}
