 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>BBC討論區</title>
  
  <link rel="stylesheet" href="../32css/styles1.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
  <header class="navbar">
    <div class="header-container">
      <div class="logo">
        <a href="http://localhost:8081/32home/HomeReview5.jsp">
          <img src="../32images/BBC.png" alt="BBC Logo">
          <span>BBC討論區</span>
        </a>
      </div>
      <nav></nav>
      <div class="header-icons">
        <input type="text" id="searchInput" class="search-input" placeholder="輸入關鍵字後按 Enter">
        <button class="search-btn" onclick="toggleSearchBox()">🔍</button>
        <ul>
          <li><a href="#">登入/註冊</a></li>
        </ul>
        <img src="../32images/B11.jpg" alt="目前是遊客身分" title="目前是遊客身分">
      </div>
    </div>
  </header>

  <main>
    <section class="hero">
      <h1>歡迎來到BBC討論區</h1>
      <p>分享您的時尚靈感，探索更多可能！</p>
    </section>
    <section id="reviews" class="content">
      <!-- 所有評價將在此動態生成 -->
    </section>
    <div id="loadMoreContainer" class="load-more-container">
      <button id="loadMoreButton" class="load-more-btn">查看更多</button>
    </div>
  </main>

  <footer class="footer">
    <p>© 2024 BBC. 版權所有。</p>
  </footer>

  <button class="floating-btn" onclick="openAddPage()">+</button>

  <script>
  function openAddPage() {
	  Swal.fire({
	    title: '新增評價',
	    html: `
	      <form id="InsertReviewForm" style="display: flex; flex-direction: column; gap: 16px;">
	        <div style="margin-bottom: 16px;">
	          <label for="userId">使用者 ID:</label>
	          <input type="text" id="userId" name="userId" required>
	        </div>
	        <div style="margin-bottom: 16px;">
	          <label for="product">產品名稱:</label>
	          <input type="text" id="product" name="product" required>
	        </div>
	        <div style="margin-bottom: 16px;">
	          <label for="product_id">產品編碼:</label>
	          <input type="text" id="product_id" name="product_id" required>
	        </div>
	        <div style="margin-bottom: 16px;">
	          <label for="content">內容:</label>
	          <textarea id="content" name="content" style="width: 100%; resize: vertical;" rows="5" required></textarea>
	        </div>
	        <div style="margin-bottom: 16px;">
	          <label for="rating">評分:</label>
	          <div id="ratingStars">
	            <i class="fa fa-star" onclick="setRating(1)"></i>
	            <i class="fa fa-star" onclick="setRating(2)"></i>
	            <i class="fa fa-star" onclick="setRating(3)"></i>
	            <i class="fa fa-star" onclick="setRating(4)"></i>
	            <i class="fa fa-star" onclick="setRating(5)"></i>
	          </div>
	          <input type="hidden" id="rating" name="rating" required>
	        </div>
	        <div style="margin-bottom: 16px;">
	          <label for="status">發表評價狀態:</label>
	          <select id="status" name="status" required>
	            <option value="active">帳號正常使用中</option>
	            <option value="deleted">帳號已刪除</option>
	            <option value="flagged">帳號異常</option>
	          </select>
	        </div>
	        <button type="button" onclick="submitReview()">提交評價</button>
	      </form>
	    `,
	    showConfirmButton: false
	  });
	}


    function submitReview() {
      const form = document.getElementById('InsertReviewForm');
      const reviewData = {
        userId: form.userId.value,
        product: form.product.value,
        productId: form.product_id.value,
        content: form.content.value,
        rating: form.rating.value,
        status: form.status.value,
      };

      fetch('http://localhost:8081/insertreviews.controller/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(reviewData),
      })
      .then(response => response.json())
      .then(data => {
        if (data.success) {
          Swal.fire('成功', '評價新增成功', 'success').then(() => {
            window.location.href = 'http://localhost:8081/32home/HomeReview5.jsp';
          });
        } else {
          Swal.fire('錯誤', data.message || '新增失敗', 'error');
        }
      })
      .catch(() => Swal.fire('錯誤', '請求失敗', 'error'));
    }
 
    
    //新增功能的讓星星顯示互動：
    function setRating(value) {
    	  const stars = document.querySelectorAll('#ratingStars .fa-star');
    	  stars.forEach((star, index) => {
    	    star.style.color = index < value ? 'gold' : 'gray';
    	  });
    	  document.getElementById('rating').value = value;
    	}


    // 動態生成評價區塊
    function addReview(review) {
      const reviewsContainer = document.getElementById("reviews");
      const reviewBlock = document.createElement("div");
      reviewBlock.className = "review-item section-block";

      // 星星
      const stars = Array(5).fill('<i class="fa fa-star" style="color: gray;"></i>');
      for (let i = 0; i < review.rating; i++) {
        stars[i] = '<i class="fa fa-star" style="color: gold;"></i>';
      }

      reviewBlock.innerHTML = `
        <div class="review-content">
          <h3>${review.product}</h3>
          <p><strong>使用者 ID:</strong> ${review.userId}</p>
          <p><strong>產品編碼:</strong> ${review.productId}</p>
          <p><strong>評價內容:</strong><br>${review.content}</p>
          <p><strong>評分:</strong> ${stars.join(' ')}</p>
          <p><strong>發表評價狀態:</strong> ${getStatusText(review.status)}</p>
          <p><strong>建立時間:</strong> ${new Date(review.createdAt).toLocaleString()}</p>
          <p><strong>最新更新時間:</strong> ${new Date(review.updatedAt).toLocaleString()}</p>
          <button onclick="confirmDelete(${review.userId}, ${review.productId})"><i class="fas fa-trash"></i> 刪除</button>
          <button class="edit-button" onclick="editReview(${review.id})"><i class="fas fa-edit"></i> 編輯</button>
        </div>
      `;

      reviewsContainer.appendChild(reviewBlock);
    }
    
 // 根據 status 顯示對應的文字
    function getStatusText(status) {
      switch (status) {
        case 'active':
          return '帳號正常使用中';
        case 'deleted':
          return '帳號已刪除';
        case 'flagged':
          return '帳號異常';
        default:
          return '未知狀態';
      }
    }
   
    
 // 編輯功能
    async function editReview(reviewId) {
  try {
    // 獲取現有的評價數據
    const response = await fetch(`http://localhost:8081/editreviews.controller/${reviewId}`);
    if (!response.ok) {
      throw new Error('無法獲取評價數據');
    }
    const review = await response.json();

    // 使用 SweetAlert 顯示編輯表單
    Swal.fire({
      title: '編輯評價',
      html: `
        <form id="EditReviewForm" style="display: flex; flex-direction: column; gap: 16px;">
          <div style="margin-bottom: 16px;">
           <label for="userId">使用者 ID:</label>
           <input type="text" id="userId" name="userId" value="${review.userId}" required>
     	 </div>
          <div style="margin-bottom: 16px;">
            <label for="product">產品名稱:</label>
            <input type="text" id="product" name="product" value="${review.product}" required>
          <div style="margin-bottom: 16px;">
          </div>
            <label for="productId">產品編碼:</label>
            <input type="text" id="productId" name="productId" value="${review.productId}" required>
          </div>
          <div style="margin-bottom: 16px;">
            <label for="content">內容:</label>
            <textarea id="content" name="content" required>${review.content}</textarea>
          </div>
          <div style="margin-bottom: 16px;">
            <label for="rating">評分:</label>
            <div id="ratingStars">
              ${Array(5).fill(0).map((_, i) => `
                <i class="fa fa-star" style="color: ${i < review.rating ? 'gold' : 'gray'};" onclick="setRating(${i + 1})"></i>
              `).join('')}
            </div>
            <input type="hidden" id="rating" name="rating" value="${review.rating}" required>
            <br>
            <div style="margin-bottom: 16px;">
            <label for="status">發表評價狀態:</label>
            <select id="status" name="status" required>
              <option value="active" ${review.status === 'active' ? 'selected' : ''}>帳號正常使用中</option>
              <option value="deleted" ${review.status === 'deleted' ? 'selected' : ''}>帳號已刪除</option>
              <option value="flagged" ${review.status === 'flagged' ? 'selected' : ''}>帳號異常</option>
            </select>
            </div>
          </form>
        </form>
      `,
      showCancelButton: true,
      confirmButtonText: '確認提交',
      cancelButtonText: '取消',
      preConfirm: () => {
        const form = document.getElementById('EditReviewForm');
        return {
          product: form.product.value,
          content: form.content.value,
          rating: document.getElementById('rating').value,
          status: form.status.value 
        };
      }
    }).then(async (result) => {
      if (result.isConfirmed) {
        const updatedReview = {
          ...review,
          ...result.value
        };

        // 提交更新
        const updateResponse = await fetch(`http://localhost:8081/updatereviews.controller/${reviewId}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(updatedReview)
        });

        if (updateResponse.ok) {
          Swal.fire('成功', '評價已更新', 'success').then(() => {
            location.reload(); // 刷新頁面以顯示更新後的數據
          });
        } else {
          Swal.fire('錯誤', '更新失敗，請稍後再試', 'error');
        }
      }
    });
  } catch (error) {
    Swal.fire('錯誤', error.message, 'error');
  }
}

// 設置星級評分
function setRating(value) {
  const stars = document.querySelectorAll('#ratingStars .fa-star');
  stars.forEach((star, index) => {
    star.style.color = index < value ? 'gold' : 'gray';
  });
  document.getElementById('rating').value = value;
}

 


    // 確認刪除評價
    async function confirmDelete(userId, productId) {
  const result = await Swal.fire({
    title: '確定要刪除此評價嗎？',
    text: '刪除後將無法復原！',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '刪除',
    cancelButtonText: '取消'
  });

  if (result.isConfirmed) {
    try {
      const response = await fetch(`/deletereviews.controller/delete?userId=${userId}&productId=${productId}`, {
        method: 'DELETE',
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `deleteUserId=${encodeURIComponent(userId)}&deleteProductId=${encodeURIComponent(productId)}`,
      });

      if (response.ok) {
        Swal.fire({
          title: '刪除成功！',
          text: '該評價已被成功刪除。',
          icon: 'success',
          timer: 2000,
          showConfirmButton: false
        }).then(() => window.location.reload());
      } else {
        Swal.fire({
          title: '刪除失敗！',
          text: '請稍後再試。',
          icon: 'error',
          timer: 2000,
          showConfirmButton: false
        });
      }
    } catch (error) {
      console.error("刪除失敗：", error);
      Swal.fire({
        title: '系統錯誤！',
        text: '請稍後再試。',
        icon: 'error',
        timer: 2000,
        showConfirmButton: false
      });
    }
  }
}
    
    


    let currentIndex = 0; // 當前顯示的評論索引
    const itemsPerPage = 10; // 每次顯示的評論數量

    function displayMoreReviews() {
      const reviewsContainer = document.getElementById("reviews");
      const loadMoreButton = document.getElementById("loadMoreButton");


      
      // 獲取下一批評論
      const nextReviews = allReviews.slice(currentIndex, currentIndex + itemsPerPage);
      nextReviews.forEach((review) => addReview(review));

      currentIndex += itemsPerPage;

      // 如果所有評論已顯示，隱藏「查看更多」按鈕
      if (currentIndex >= allReviews.length) {
        loadMoreButton.style.display = "none";
      }
    }

    // 從後端獲取所有評價
    let allReviews = [];
    fetch("../getallreviews.controller")
      .then((response) => response.json())
      .then((reviews) => {
        allReviews = reviews;
        displayMoreReviews(); // 初始顯示第一批評論
      })
      .catch((error) => {
        console.error("無法取得評價資料：", error);
      });

    // 綁定「查看更多」按鈕事件
    document.getElementById("loadMoreButton").addEventListener("click", displayMoreReviews);

 // 控制搜尋框顯示/隱藏
    function toggleSearchBox() {
      const searchInput = document.getElementById("searchInput");
      searchInput.style.display = searchInput.style.display === "none" ? "block" : "none";
      searchInput.focus();
    }

    // 搜尋功能
    function searchReviews() {
      const searchInput = document.getElementById("searchInput").value.toLowerCase();
      const reviewsContainer = document.getElementById("reviews");

      // 清空原有顯示的評論
      reviewsContainer.innerHTML = "";
      
     


      // 過濾符合條件的評論
      const filteredReviews = allReviews.filter((review) =>
        review.product.toLowerCase().includes(searchInput) ||
        review.userId.toString().includes(searchInput) ||
        review.productId.toString().includes(searchInput) ||
        review.content.toLowerCase().includes(searchInput)
      );

      if (filteredReviews.length === 0) {
        reviewsContainer.innerHTML = `<p style="color: white;">找不到符合條件的結果。</p>`;
      } else {
        filteredReviews.forEach((review) => addReview(review));
      }
    }

    // 綁定搜尋框按鍵事件（按下 Enter 觸發搜尋）
    document.getElementById("searchInput").addEventListener("keypress", (event) => {
      if (event.key === "Enter") {
        searchReviews();
      }
    });

   
  </script>
</body>
</html>