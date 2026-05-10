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
                    List.of("Chilonzor", "Yunusobod", "Yakkasaroy", "Mirzo Ulug'bek", "Sergeli", "Mirobot"));

            // 2. Toshkent viloyati
            saveRegionWithDistricts("Toshkent viloyati",
                    List.of("Chirchiq", "Angren", "Olmaliq", "Bekobod", "Qibray"));

            // 3. Samarqand viloyati
            saveRegionWithDistricts("Samarqand viloyati",
                    List.of("Samarqand shahri", "Pastdarg'om", "Ishtixon", "Bulung'ur"));

            // 4. Farg'ona viloyati
            saveRegionWithDistricts("Farg'ona viloyati",
                    List.of("Farg'ona shahri", "Marg'ilon", "Qo'qon", "Quva"));

            // 5. Qoraqalpog'iston Respublikasi
            saveRegionWithDistricts("Qoraqalpog'iston Respublikasi",
                    List.of("Nukus shahri", "Qo'ng'irot", "Mo'ynoq", "Beruniy", "Sho'manay", "Shimbay"));

            // Shu tartibda qolgan viloyatlarni ham qo'shishing mumkin:
            // Andijon, Namangan, Buxoro, Navoiy, Qashqadaryo, Surxondaryo, Jizzax, Sirdaryo, Xorazm.

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
